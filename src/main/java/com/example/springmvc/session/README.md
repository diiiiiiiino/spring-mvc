## 세션 동작 방식
* 사용자가 loginId , password 정보를 전달하면 서버에서 해당 사용자가 맞는지 확인한다
* 세션 ID를 생성하는데, 추정 불가능해야 한다.
  * UUID는 추정이 불가능하다.
    * Cookie: mySessionId=zz0101xx-bab9-4b92-9b32-dadb280f4b61
* 생성된 세션 ID와 세션에 보관할 값( memberA )을 서버의 세션 저장소에 보관한다.

## 세션 보안
* 쿠키 값을 변조 가능 -> 예상 불가능한 복잡한 세션Id를 사용한다.
* 쿠키에 보관하는 정보는 클라이언트 해킹시 털릴 가능성이 있다. -> 세션Id가 털려도 여기에는 중요한 정보가 없
다.
* 쿠키 탈취 후 사용 -> 해커가 토큰을 털어가도 시간이 지나면 사용할 수 없도록 서버에서 세션의 만료시간을 짧게(예: 30분) 유지한다. <br> 
  또는 해킹이 의심되는 경우 서버에서 해당 세션을 강제로 제거하면 된다

## HttpSession
* 서블릿이 제공하는 HttpSession 도 결국 우리가 직접 만든 SessionManager 와 같은 방식으로 동작한다.
* 서블릿을 통해 HttpSession 을 생성하면 다음과 같은 쿠키를 생성한다. 쿠키 이름이 JSESSIONID 이고, 값은 추정 불가능한 랜덤 값이다.
* Cookie: JSESSIONID=5B78E23B513F50164D6FDD8C97B0AD05

## HttpSession getSession
* request.getSession(true)
  * 세션이 있으면 기존 세션을 반환한다.
  * 세션이 없으면 새로운 세션을 생성해서 반환한다.
* request.getSession(false)
  * 세션이 있으면 기존 세션을 반환한다.
  * 세션이 없으면 새로운 세션을 생성하지 않는다. null 을 반환한다.
* request.getSession() : 신규 세션을 생성하는 request.getSession(true) 와 동일하다.

## TrackingModes
* 로그인을 처음 시도하면 URL이 다음과 같이 jsessionid 를 포함하고 있는 것을 확인할 수 있다.
* http://localhost:8080/;jsessionid=F59911518B921DF62D09F0DF8F83F872
* 이것은 웹 브라우저가 쿠키를 지원하지 않을 때 쿠키 대신 URL을 통해서 세션을 유지하는 방법이다. 이 방법을 사용하
  려면 URL에 이 값을 계속 포함해서 전달해야 한다. 타임리프 같은 템플릿은 엔진을 통해서 링크를 걸면 jsessionid
  를 URL에 자동으로 포함해준다. 서버 입장에서 웹 브라우저가 쿠키를 지원하는지 하지 않는지 최초에는 판단하지 못하
  므로, 쿠키 값도 전달하고, URL에 jsessionid 도 함께 전달한다.
* URL 전달 방식을 끄고 항상 쿠키를 통해서만 세션을 유지하고 싶으면 다음 옵션을 넣어주면 된다. 이렇게 하면 URL에
  jsessionid 가 노출되지 않는다.
  ```
  application.properties
  server.servlet.session.tracking-modes=cookie
  ```
  
##  jsessionid가 url에 있을때 404 오류가 발생
* 스프링에서 최근 URL 매핑 전략이 변경 되었습니다. 따라서 다음과 같이 출력될 때 컨트롤러를 찾지 못하고 404 오류 가 발생할 수 있다. 
  ```
  http://localhost:8080/;jsessionid=F59911518B921DF62D09F0DF8F83F872
  ```
* 해결방안은 session.tracking-modes 를 사용 
  ```
  server.servlet.session.tracking-modes=cookie
  ```
* 만약 URL에 jsessionid가 꼭 필요하다면 application.properties에 다음 옵션을 추가해주세요. 
  ```
  spring.mvc.pathmatch.matching-strategy=ant_path_matcher
  ```
## HttpSession 속성
* sessionId : 세션Id, JSESSIONID 의 값이다. 예) 34B14F008AA3527C9F8ED620EFD7A4E1
* maxInactiveInterval : 세션의 유효 시간, 예) 1800초, (30분)
* creationTime : 세션 생성일시
* lastAccessedTime : 세션과 연결된 사용자가 최근에 서버에 접근한 시간, 클라이언트에서 서버로
* sessionId ( JSESSIONID )를 요청한 경우에 갱신된다.
* isNew : 새로 생성된 세션인지, 아니면 이미 과거에 만들어졌고, 클라이언트에서 서버로 sessionId ( JSESSIONID )를 요청해서 조회된 세션인지 여부

## 세션 타임아웃 설정
* 세션은 사용자가 로그아웃을 직접 호출해서 session.invalidate() 가 호출 되는 경우에 삭제된다. 그런데 대부분
  의 사용자는 로그아웃을 선택하지 않고, 그냥 웹 브라우저를 종료한다. 문제는 HTTP가 비 연결성(ConnectionLess)
  이므로 서버 입장에서는 해당 사용자가 웹 브라우저를 종료한 것인지 아닌지를 인식할 수 없다. 따라서 서버에서 세션
  데이터를 언제 삭제해야 하는지 판단하기가 어렵다.
* 이 경우 남아있는 세션을 무한정 보관하면 다음과 같은 문제가 발생할 수 있다.
  * 세션과 관련된 쿠키( JSESSIONID )를 탈취 당했을 경우 오랜 시간이 지나도 해당 쿠키로 악의적인 요청을 할 수
  있다.
  * 세션은 기본적으로 메모리에 생성된다. 메모리의 크기가 무한하지 않기 때문에 꼭 필요한 경우만 생성해서 사용해
  야 한다. 10만명의 사용자가 로그인하면 10만개의 세션이 생성되는 것이다.
### 세션의 종료 시점
* 세션의 종료 시점을 어떻게 정하면 좋을까? 가장 단순하게 생각해보면, 세션 생성 시점으로부터 30분 정도로 잡으면 될
  것 같다. 그런데 문제는 30분이 지나면 세션이 삭제되기 때문에, 열심히 사이트를 돌아다니다가 또 로그인을 해서 세션
  을 생성해야 한다 그러니까 30분 마다 계속 로그인해야 하는 번거로움이 발생한다.
  더 나은 대안은 세션 생성 시점이 아니라 사용자가 서버에 최근에 요청한 시간을 기준으로 30분 정도를 유지해주는 것
  이다. 이렇게 하면 사용자가 서비스를 사용하고 있으면, 세션의 생존 시간이 30분으로 계속 늘어나게 된다. 따라서 30
  분 마다 로그인해야 하는 번거로움이 사라진다. HttpSession 은 이 방식을 사용한다
### 세션 타임아웃 설정
* 스프링 부트로 글로벌 설정
  ```
  application.properties
  server.servlet.session.timeout=60 : 60초, 기본은 1800(30분)
  (글로벌 설정은 분 단위로 설정해야 한다. 60(1분), 120(2분), ...)
  ```
* 특정 세션 단위로 시간 설정 
  ```
  session.setMaxInactiveInterval(1800); //1800초
  ```
### 세션 타임아웃 발생
* 세션의 타임아웃 시간은 해당 세션과 관련된 JSESSIONID 를 전달하는 HTTP 요청이 있으면 현재 시간으로 다시 초기화 된다. 
  이렇게 초기화 되면 세션 타임아웃으로 설정한 시간동안 세션을 추가로 사용할 수 있다.
  session.getLastAccessedTime() : 최근 세션 접근 시간
  LastAccessedTime 이후로 timeout 시간이 지나면, WAS가 내부에서 해당 세션을 제거한다.

### 세션 사용 주의점
* 세션에는 최소한의 데이터만 보관해야 한다는 점이다. 보관한 데이터 용량 * 사용자 수로 세션의 메모리 사용량이 급격하게 늘어나서 장애로 이어질 수 있다. 
* 추가로 세션의 시간을 너무 길게 가져가면 메모리 사용이 계속 누적 될 수 있으므로 적당한 시간을 선택하는 것이 필요하다. 
  기본이 30분이라는 것을 기준으로 고민하면 된다.