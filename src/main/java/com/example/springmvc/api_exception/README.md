## BasicErrorController
*  HTML 페이지를 제공하는 경우에는 매우 편리
```
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse
response) {}
@RequestMapping
public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {}
```
* errorHtml() : produces = MediaType.TEXT_HTML_VALUE : 클라이언트 요청의 Accept 해더 값이
  text/html 인 경우에는 errorHtml() 을 호출해서 view를 제공한다.
* error() : 그외 경우에 호출되고 ResponseEntity 로 HTTP Body에 JSON 데이터를 반환한다
* 스프링 부트는 BasicErrorController 가 제공하는 기본 정보들을 활용해서 오류 API를 생성해준다.
  다음 옵션들을 설정하면 더 자세한 오류 정보를 추가할 수 있다.
  * server.error.include-binding-errors=always
  * server.error.include-exception=true
  * server.error.include-message=always
  * server.error.include-stacktrace=always

## HandlerExceptionResolver
* 스프링 MVC는 컨트롤러(핸들러) 밖으로 예외가 던져진 경우 예외를 해결하고, 동작을 새로 정의할 수 있는 방법을 제
  공한다. 컨트롤러 밖으로 던져진 예외를 해결하고, 동작 방식을 변경하고 싶으면 HandlerExceptionResolver 를
  사용하면 된다. 줄여서 ExceptionResolver 라 한다.
* ExceptionResolver 로 예외를 해결해도 postHandle() 은 호출되지 않는다
* ExceptionResolver 가 ModelAndView 를 반환하는 이유는 마치 try, catch를 하듯이, Exception 을 처리해서 정상 흐름 처럼 변경하는 것이 목적이다. 이름 그대로 Exception 을 Resolver(해결)하는 것이 목적이
  다
### 반환 값에 따른 동작 방식
HandlerExceptionResolver 의 반환 값에 따른 DispatcherServlet 의 동작 방식은 다음과 같다.
* 빈 ModelAndView: new ModelAndView() 처럼 빈 ModelAndView 를 반환하면 뷰를 렌더링 하지 않고, 정상 흐름으로 서블릿이 리턴된다.
* ModelAndView 지정: ModelAndView 에 View , Model 등의 정보를 지정해서 반환하면 뷰를 렌더링 한다.
* null: null 을 반환하면, 다음 ExceptionResolver 를 찾아서 실행한다. 만약 처리할 수 있는 
  ExceptionResolver 가 없으면 예외 처리가 안되고, 기존에 발생한 예외를 서블릿 밖으로 던진다.

### ExceptionResolver 활용
* 예외 상태 코드 변환
  * 예외를 response.sendError(xxx) 호출로 변경해서 서블릿에서 상태 코드에 따른 오류를 처리하도
  록 위임
  * 이후 WAS는 서블릿 오류 페이지를 찾아서 내부 호출, 예를 들어서 스프링 부트가 기본으로 설정한 /
  error 가 호출됨
* 뷰 템플릿 처리
  * ModelAndView 에 값을 채워서 예외에 따른 새로운 오류 화면 뷰 렌더링 해서 고객에게 제공
* API 응답 처리
  * response.getWriter().println("hello"); 처럼 HTTP 응답 바디에 직접 데이터를 넣어주는
  것도 가능하다. 여기에 JSON 으로 응답하면 API 응답 처리를 할 수 있다.

## configureHandlerExceptionResolvers
* configureHandlerExceptionResolvers(..) 를 사용하면 스프링이 기본으로 등록하는 
  ExceptionResolver 가 제거되므로 주의, extendHandlerExceptionResolvers 를 사용하자.

## 스프링 기본 제공 ExceptionResolver
* HandlerExceptionResolverComposite 에 다음 순서로 등록
1. ExceptionHandlerExceptionResolver
2. ResponseStatusExceptionResolver
3. DefaultHandlerExceptionResolver 우선 순위가 가장 낮다.

### ResponseStatusExceptionResolver
* ResponseStatusExceptionResolver 는 예외에 따라서 HTTP 상태 코드를 지정해주는 역할을 한다.
* 두 가지 경우를 처리한다
  * @ResponseStatus 가 달려있는 예외
  * ResponseStatusException 예외
### @ResponseStatus
* MessageSource에서 찾는 기능도 제공한다. 
  ```reason = "error.bad"```
* 개발자가 직접 변경할 수 없는 예외에는 적용할 수 없다.
* 조건에 따라 동적으로 변경하는 것도 어려움

### ResponseStatusException
* 개발자가 직접 변경할 수 없는 예외에는 적용 가능
* 조건에 따라 동적으로 변경 가능

### DefaultHandlerExceptionResolver
* 스프링 내부에서 발생하는 스프링 예외를 해결한다.
* 대표적으로 파라미터 바인딩 시점에 타입이 맞지 않으면 내부에서 TypeMismatchException 이 발생하는데
  500 오류가 아니라 HTTP 상태 코드 400 오류로 변경한다.
* DefaultHandlerExceptionResolver.handleTypeMismatch 를 보면 다음과 같은 코드를 확인할 수 있다.
  response.sendError(HttpServletResponse.SC_BAD_REQUEST) (400)
  결국 response.sendError() 를 통해서 문제를 해결한다.
  sendError(400) 를 호출했기 때문에 WAS에서 다시 오류 페이지( /error )를 내부 요청한다.

## @ExceptionHandler
* ExceptionHandlerExceptionResolver 를 기본으로 제공하고, 기본으로 제공하는 ExceptionResolver
  중에 우선순위도 가장 높다. 실무에서 API 예외 처리는 대부분 이 기능을 사용한다
* @ExceptionHandler 애노테이션을 선언하고, 해당 컨트롤러에서 처리하고 싶은 예외를 지정해주면 된다. 해당 컨
  트롤러에서 예외가 발생하면 이 메서드가 호출된다. 참고로 지정한 예외 또는 그 예외의 자식 클래스는 모두 잡을 수 있
  다
### 우선순위
* 스프링의 우선순위는 항상 자세한 것이 우선권을 가진다. 예를 들어서 부모, 자식 클래스가 있고 다음과 같이 예외가 처
  리된다.
```
@ExceptionHandler(부모예외.class)
public String 부모예외처리()(부모예외 e) {}

@ExceptionHandler(자식예외.class)
public String 자식예외처리()(자식예외 e) {}
```

### 다양한 예외
* 다음과 같이 다양한 예외를 한번에 처리할 수 있다.
```
@ExceptionHandler({AException.class, BException.class})
public String ex(Exception e) {
 log.info("exception e", e);
}
```

### 예외 생략
* @ExceptionHandler 에 예외를 생략할 수 있다. 생략하면 메서드 파라미터의 예외가 지정된다
```
@ExceptionHandler
public ResponseEntity<ErrorResult> userExHandle(UserException e) {}
```

### IllegalArgumentException 처리
```
@ResponseStatus(HttpStatus.BAD_REQUEST)
@ExceptionHandler(IllegalArgumentException.class)
public ErrorResult illegalExHandle(IllegalArgumentException e) {
 log.error("[exceptionHandle] ex", e);
 return new ErrorResult("BAD", e.getMessage());
}

@ExceptionHandler
public ResponseEntity<ErrorResult> userExHandle(UserException e) {
 log.error("[exceptionHandle] ex", e);
 ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
 return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
}
```
* 실행 흐름
* 컨트롤러를 호출한 결과 IllegalArgumentException 예외가 컨트롤러 밖으로 던져진다.
* 예외가 발생했으로 ExceptionResolver 가 작동한다. 가장 우선순위가 높은
  ExceptionHandlerExceptionResolver 가 실행된다.
* ExceptionHandlerExceptionResolver 는 해당 컨트롤러에 IllegalArgumentException 을 처리
  할 수 있는 @ExceptionHandler 가 있는지 확인한다.
* illegalExHandle() 를 실행한다. @RestController 이므로 illegalExHandle() 에도 
  @ResponseBody 가 적용된다. 따라서 HTTP 컨버터가 사용되고, 응답이 다음과 같은 JSON으로 반환된다.
* @ResponseStatus(HttpStatus.BAD_REQUEST) 를 지정했으므로 HTTP 상태 코드 400으로 응답한다

## @ControllerAdvice
* 정상 코드와 예외 처리 코드가 하나의 컨트롤러에서 분리가 가능하다 
* @ControllerAdvice 는 대상으로 지정한 여러 컨트롤러에 @ExceptionHandler , @InitBinder 기능
  을 부여해주는 역할을 한다.
* @ControllerAdvice 에 대상을 지정하지 않으면 모든 컨트롤러에 적용된다. (글로벌 적용)
* @RestControllerAdvice 는 @ControllerAdvice 와 같고, @ResponseBody 가 추가되어 있다.
  @Controller , @RestController 의 차이와 같다.

### 대상 컨트롤러 지정 방법
```
// Target all Controllers annotated with @RestController
@ControllerAdvice(annotations = RestController.class)
public class ExampleAdvice1 {}

// Target all Controllers within specific packages
@ControllerAdvice("org.example.controllers")
public class ExampleAdvice2 {}

// Target all Controllers assignable to specific classes
@ControllerAdvice(assignableTypes = {ControllerInterface.class,
AbstractController.class})
public class ExampleAdvice3 {}
```