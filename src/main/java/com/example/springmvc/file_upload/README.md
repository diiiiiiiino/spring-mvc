## HTML 폼 전송 방식
* application/x-www-form-urlencoded
* multipart/form-data
### application/x-www-form-urlencoded
* Form 태그에 별도의 enctype 옵션이 없으면 웹 브라우저는 요청 HTTP 메시지의 헤더에 다음 내용을 추가한다
  ```Content-Type: application/x-www-form-urlencoded```
* 폼에 입력한 전송할 항목을 HTTP Body에 문자로 username=kim&age=20 와 같이 & 로 구분해서 전송한다.
### multipart/form-data
* 이 방식을 사용하려면 Form 태그에 별도의 enctype="multipart/form-data" 를 지정해야 한다.
* 다른 종류의 여러 파일과 폼의 내용 함께 전송할 수 있다
* 폼의 입력 결과로 생성된 HTTP 메시지를 보면 각각의 전송 항목이 구분이 되어있다. Content-Disposition 이라
  는 항목별 헤더가 추가되어 있고 여기에 부가 정보가 있다. 예제에서는 username , age , file1 이 각각 분리되어
  있고, 폼의 일반 데이터는 각 항목별로 문자가 전송되고, 파일의 경우 파일 이름과 Content-Type이 추가되고 바이너리
  데이터가 전송된다.
* multipart/form-data 는 이렇게 각각의 항목을 구분해서, 한번에 전송하는 것이다.
* ```
  Content-Type: multipart/form-data; boundary=----xxxx
   ------xxxx
   Content-Disposition: form-data; name="itemName"
   Spring
   ------xxxx
   Content-Disposition: form-data; name="file"; filename="test.data"
   Content-Type: application/octet-stream
   sdklajkljdf...
  ```

## 멀티파트 사용 옵션
### 업로드 사이즈 제한
* 큰 파일을 무제한 업로드하게 둘 수는 없으므로 업로드 사이즈를 제한할 수 있다.
  사이즈를 넘으면 예외( SizeLimitExceededException )가 발생한다
```
spring.servlet.multipart.max-file-size=1MB  파일 하나의 최대 사이즈, 기본 1MB
spring.servlet.multipart.max-request-size=10MB 멀티파트 요청 하나에 여러 파일을 업로드 할 수 있는데, 그 전체 합이다. 기본 10MB
```

### spring.servlet.multipart.enabled 끄기
```spring.servlet.multipart.enabled=false```

### spring.servlet.multipart.enabled 켜기
```spring.servlet.multipart.enabled=true (기본 true)```
* 이 옵션을 켜면 스프링 부트는 서블릿 컨테이너에게 멀티파트 데이터를 처리하라고 설정한다. 참고로 기본 값은 true이다.
* request.getParameter("itemName") 의 결과도 잘 출력되고, request.getParts() 에도 요청한 두 가지
  멀티파트의 부분 데이터가 포함된 것을 확인할 수 있다. 이 옵션을 켜면 복잡한 멀티파트 요청을 처리해서 사용할 수 있
  게 제공한다.
* 로그를 보면 HttpServletRequest 객체가 RequestFacade -> StandardMultipartHttpServletRequest 로 변한 것을 확인할 수 있다.
* spring.servlet.multipart.enabled 옵션을 켜면 스프링의 DispatcherServlet 에서 멀티파트 리
  졸버( MultipartResolver )를 실행한다.
  멀티파트 리졸버는 멀티파트 요청인 경우 서블릿 컨테이너가 전달하는 일반적인 HttpServletRequest 를
  MultipartHttpServletRequest 로 변환해서 반환한다.
  MultipartHttpServletRequest 는 HttpServletRequest 의 자식 인터페이스이고, 멀티파트와 관련
  된 추가 기능을 제공한다.
* 스프링이 제공하는 기본 멀티파트 리졸버는 MultipartHttpServletRequest 인터페이스를 구현한
  StandardMultipartHttpServletRequest 를 반환한다.
  이제 컨트롤러에서 HttpServletRequest 대신에 MultipartHttpServletRequest 를 주입받을 수 있
  는데, 이것을 사용하면 멀티파트와 관련된 여러가지 처리를 편리하게 할 수 있다. 그런데 이후 강의에서 설명할
  MultipartFile 이라는 것을 사용하는 것이 더 편하기 때문에 MultipartHttpServletRequest 를 잘
  사용하지는 않는다. 더 자세한 내용은 MultipartResolver 를 검색해보자

##  request.getParts()
* 멀티파트 형식은 전송 데이터를 하나하나 각각 부분( Part )으로 나누어 전송한다. parts 에는 이렇게 나누어진 데이
터가 각각 담긴다.
* 서블릿이 제공하는 Part 는 멀티파트 형식을 편리하게 읽을 수 있는 다양한 메서드를 제공한다.
  * part.getSubmittedFileName() : 클라이언트가 전달한 파일명
  * part.getInputStream(): Part의 전송 데이터를 읽을 수 있다.
  * part.write(...): Part를 통해 전송된 데이터를 저장할 수 있다

## MultipartFile
* 스프링은 MultipartFile 이라는 인터페이스로 멀티파트 파일을 매우 편리하게 지원한다.
* 업로드하는 HTML Form의 name에 맞추어 @RequestParam 을 적용하면 된다. 
  추가로 @ModelAttribute 에서도 MultipartFile 을 동일하게 사용할 수 있다
### 주요 메서드
* file.getOriginalFilename() : 업로드 파일 명
* file.transferTo(...) : 파일 저장