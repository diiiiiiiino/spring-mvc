## @ResponseBody 사용원리
- HTTP의 BODY에 문자 내용을 직접 반환
- viewResolver 대신에 HttpMessageConverter 가 동작
- 기본 문자처리: StringHttpMessageConverter
- 기본 객체처리: MappingJackson2HttpMessageConverter
- byte 처리 등등 기타 여러 HttpMessageConverter가 기본으로 등록되어 있음
- 응답의 경우 클라이언트의 HTTP Accept 헤더와 서버의 컨트롤러 반환 타입 정보를 조합해서 HttpMessageConverter가 선택된다.

## Http 메시지 컨버터 적용
- HTTP 요청 : @RequestBody, HttpEntity(RequestEntity)
- HTTP 응답 : @ResponseBody, HttpEntity(ResponseEntity)

## HttpMessageConverter
- canRead() , canWrite() : 메시지 컨버터가 해당 클래스, 미디어타입을 지원하는지 체크
- read() , write() : 메시지 컨버터를 통해서 메시지를 읽고 쓰는 기능

## 스프링 부트 기본 메시지 컨버터
- ByteArrayHttpMessageConverter : byte[] 데이터를 처리한다. 
  - 클래스 타입: byte[] , 미디어타입: */*
  - 요청 예) @RequestBody byte[] data
  - 응답 예) @ResponseBody return byte[] 쓰기 미디어타입 application/octet-stream
- StringHttpMessageConverter : String 문자로 데이터를 처리한다.
  - 클래스 타입: String , 미디어타입: */*
  - 요청 예) @RequestBody String data
  - 응답 예) @ResponseBody return "ok" 쓰기 미디어타입 text/plain
- MappingJackson2HttpMessageConverter :  application/json
  - 클래스 타입: 객체 또는 HashMap , 미디어타입 application/json 관련
  - 요청 예) @RequestBody HelloData data
  - 응답 예) @ResponseBody return helloData 쓰기 미디어타입 application/json 관련
- 스프링 부트는 다양한 메시지 컨버터를 제공하는데, 대상 클래스 타입과 미디어 타입 둘을 체크해서 사용여부를 결정한다. <br> 
  만약 만족하지 않으면 다음 메시지 컨버터로 우선순위가 넘어간다.

## HTTP 요청 데이터 읽기
- HTTP 요청이 오고, 컨트롤러에서 @RequestBody , HttpEntity 파라미터를 사용한다.
- 메시지 컨버터가 메시지를 읽을 수 있는지 확인하기 위해 canRead() 를 호출한다.
  - 대상 클래스 타입을 지원하는가.
    - 예) @RequestBody 의 대상 클래스 ( byte[] , String , HelloData )
  - HTTP 요청의 Content-Type 미디어 타입을 지원하는가.
    - 예) text/plain , application/json , */*
- canRead() 조건을 만족하면 read() 를 호출해서 객체 생성하고, 반환한다

## HTTP 응답 데이터 생성
- 컨트롤러에서 @ResponseBody , HttpEntity 로 값이 반환된다.
- 메시지 컨버터가 메시지를 쓸 수 있는지 확인하기 위해 canWrite() 를 호출한다.
  - 대상 클래스 타입을 지원하는가.
    - 예) return의 대상 클래스 ( byte[] , String , HelloData )
  - HTTP 요청의 Accept 미디어 타입을 지원하는가.(더 정확히는 @RequestMapping 의 produces )
    - 예) text/plain , application/json , */*
- canWrite() 조건을 만족하면 write() 를 호출해서 HTTP 응답 메시지 바디에 데이터를 생성한다.

## RequestMappingHandlerAdapter 동작 방식
1. 컨트롤러의 파라미터, 애노테이션 정보를 기반으로 전달 데이터 생성
2. 핸들러 호출
3. 컨트롤러의 반환 값을 반환

## ArgumentResolver
- HTTP 요청 파라미터를 유연하게 처리해주는 객체다
- RequestMappingHandlerAdapter가 ArgumentResolver를 호출해서 핸들러가 필요로 하는 다양한 파라미터의 값을 생성한다
- 파라미터의 값이 모두 준비되면 핸들러를 호출하면서 값을 넘겨준다.
- 동작방식
  - ArgumentResolver 의 supportsParameter() 를 호출해서 해당 파라미터를 지원하는지 체크하고, 지원하면
    resolveArgument() 를 호출해서 실제 객체를 생성한다. 그리고 이렇게 생성된 객체가 컨트롤러 호출시 넘어가는
    것이다.
  - 원한다면 여러분이 직접 이 인터페이스를 확장해서 원하는 ArgumentResolver 를 만들 수도 있다.

## ReturnValueHandler
- HTTP 응답 값을 변환하고 처리한다
- 컨트롤러에서 String으로 뷰 이름을 반환해도, 동작하는 이유가 바로 ReturnValueHandler 덕분이다.

## HTTP 메시지 컨버터 위치
* 요청의 경우 @RequestBody 를 처리하는 ArgumentResolver 가 있고, HttpEntity 를 처리하는
  ArgumentResolver 가 있다. 이 ArgumentResolver 들이 HTTP 메시지 컨버터를 사용해서 필요한 객체를 생성
  하는 것이다. (어떤 종류가 있는지 코드로 살짝 확인해보자)
* 응답의 경우 @ResponseBody 와 HttpEntity 를 처리하는 ReturnValueHandler 가 있다. 그리고 여기에서
  HTTP 메시지 컨버터를 호출해서 응답 결과를 만든다.
* 스프링 MVC는 @RequestBody @ResponseBody 가 있으면 RequestResponseBodyMethodProcessor()
  HttpEntity 가 있으면 HttpEntityMethodProcessor() 를 사용한다.

## 확장
- 스프링은 다음을 모두 인터페이스로 제공한다. 따라서 필요하면 언제든지 기능을 확장할 수 있다.
* HandlerMethodArgumentResolver
* HandlerMethodReturnValueHandler
* HttpMessageConverter