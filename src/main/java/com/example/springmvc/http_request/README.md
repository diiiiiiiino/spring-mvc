## HTTP 메서드
- @RequestMapping에 method 속성으로 HTTP 메서드를 지정하지 않으면 메서드와 무관하게 호출됨

## @ModelAttribute
* 처리 순서
  1. HelloData 객체 생성
  2. 요청 파라미터의 이름으로 HelloData객체의 프로퍼티를 찾는다. 그리고 해당 프로퍼티의 setter를 호출해서 파라미터의 값을 입력 한다.
  3. 파라미터 이름이 username이면 setUsername() 메서드를 찾아서 호출하면서 값을 입력한다.
* 생략
  - @ModelAttribute과 @RequestParam은 생략 가능한데 둘다 생략 시에는 아래와 같은 규칙이 적용된다.
    - String, int, Integer 같은 단순 타입 : @RequestParam
    - 나머지 : @ModelAttribute (argument resolver로 지정해둔 타입 외)

## HTTP message body에 데이터를 직접 담아서 요청
- HTTP API에서 주로 사용, JSON, XML, TEXT
- 주로 JSON 사용
- POST, PUT, PATCH
- @RequestParam, @ModelAttribute는 요청 파라미터에만 사용가능하다 (HTML Form 형식으로 전달되는 경우도 포함)

## HttpEntity
- HTTP header, body 정보를 편리하게 조회
- 메시지 바디 정보를 직접 조회
- 요청 파라미터를 조회하는 기능과 관계 없음 @RequestParam X, @ModelAttribute X
- HttpEntity는 응답에도 사용 가능
  - 메시지 바디 정보 직접 반환
  - 헤더 정보 포함 가능
  - view 조회X
- HttpEntity 를 상속받은 다음 객체들도 같은 기능을 제공한다.
  - RequestEntity
    - HttpMethod, url 정보가 추가, 요청에서 사용
  - ResponseEntity
    - HTTP 상태 코드 설정 가능, 응답에서 사용
    - return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED)

## @RequestBody
- @RequestBody 를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회할 수 있다. 참고로 헤더 정보가 필요하다면
  HttpEntity 를 사용하거나 @RequestHeader 를 사용하면 된다.
- 이렇게 메시지 바디를 직접 조회하는 기능은 요청 파라미터를 조회하는 @RequestParam , @ModelAttribute 와
  는 전혀 관계가 없다.
- JSON 요청 -> HTTP 메세지 컨버터 -> 객체

## @ResponseBody
- @ResponseBody 를 사용하면 응답 결과를 HTTP 메시지 바디에 직접 담아서 전달할 수 있다.
- 물론 이 경우에도 view를 사용하지 않는다
- 객체 -> HTTP 메세지 컨버터 -> JSON 응답

## 요청 파라미터 vs HTTP 메시지 바디
- 요청 파라미터를 조회하는 기능: @RequestParam , @ModelAttribute
- HTTP 메시지 바디를 직접 조회하는 기능: @RequestBody

## HTTP 응답 - 정적 리소스, 뷰 템플릿
- 정적 리소스
  - 브라우저에 정적인 HTML, css, js를 제공할 때
  - 스프링 부트는 클래스패스의 다음 디렉토리에 있는 정적 리소스를 제공한다
  - /static , /public , /resources , /META-INF/resources
  - src/main/resources 는 리소스를 보관하는 곳이고, 또 클래스패스의 시작 경로이다
- 뷰 템플릿
  - 브라우저에 동적인 HTML을 제공할 때
  - 뷰 템플릿을 거쳐서 HTML이 생성되고, 뷰가 응답을 만들어서 전달한다
  - 스프링 부트는 기본 뷰 템플릿 경로를 제공한다 (src/main/resources/templates)
- HTTP 메세지 사용
  - HTTP API를 제공하는 경우

## String을 반환하는 경우 - View or HTTP 메시지
- @ResponseBody 가 없으면 response/hello 로 뷰 리졸버가 실행되어서 뷰를 찾고, 렌더링 한다.
- @ResponseBody 가 있으면 뷰 리졸버를 실행하지 않고, HTTP 메시지 바디에 직접 response/hello 라는 문자가
  입력된다
## Void를 반환하는 경우
- @Controller 를 사용하고, HttpServletResponse , OutputStream(Writer) 같은 HTTP 메시지 바
  디를 처리하는 파라미터가 없으면 요청 URL을 참고해서 논리 뷰 이름으로 사용
  - 요청 URL: /response/hello
  - 실행: templates/response/hello.html
- 참고로 이 방식은 명시성이 너무 떨어지고 이렇게 딱 맞는 경우도 많이 없어서, 권장하지 않는다

## HTTP 메시지
- @ResponseBody , HttpEntity 를 사용하면, 뷰 템플릿을 사용하는 것이 아니라, HTTP 메시지 바디에 직접 응답
  데이터를 출력할 수 있다

## @RestController
- @Controller + @ResponseBody = @RestController
- HTTP 메세지 바디에 직업 데이터를 입력
- Rest API를 만들때 사용