## @ServletComponentScan
- 스프링 부트에서 서블릿을 직접 등록해서 사용할 수 있게 하는 어노테이션

## Content-Length
- HTTP 응답 시 WAS가 자동으로 생성

## HttpServletRequest
- HTTP 요청 메세지를 편리하게 사용하도록 파싱한 객체

## HttpServletResponse
- HTTP 응답 메세지를 편리하게 사용하도록 파싱한 객체

## POST HTML FORM
- content-type : application/x-www-form-urlencoded
- 메세지 바디에 쿼리 파라미터 형식으로 데이터 전달 -> username=hello&age=20

## content-type
- HTTP 메세지 바디의 데이터 형식 지정
- GET 메서드는 메세지 바디를 사용하지 않기 때문에 content-type이 없음
- POST HTML Form 형식은 메세지 바디에 데이터를 포함하기 때문에 content-type 지정 필수

## application/json
- 스펙상 utf-8 형식을 사용하도록 정의됨
- 따라서 application/json;charset=utf-8은 의미없음