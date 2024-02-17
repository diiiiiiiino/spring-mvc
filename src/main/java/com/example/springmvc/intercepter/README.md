## 스프링 인터셉터
### 스프링 인터셉터 흐름
* HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 스프링 인터셉터 -> 컨트롤러
* 스프링 인터셉터는 디스패처 서블릿과 컨트롤러 사이에서 컨트롤러 호출 직전에 호출 된다.
* 스프링 인터셉터는 스프링 MVC가 제공하는 기능이기 때문에 결국 디스패처 서블릿 이후에 등장하게 된다. 스프링 MVC의 시작점이 디스패처 서블릿이라고 생각해보면 이해가 될 것이다.
* 스프링 인터셉터에도 URL 패턴을 적용할 수 있는데, 서블릿 URL 패턴과는 다르고, 매우 정밀하게 설정할 수 있다.

### 스프링 인터셉터 제한
* HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 스프링 인터셉터 -> 컨트롤러 //로그인 사용자
* HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 스프링 인터셉터(적절하지 않은 요청이라 판단, 컨트롤러 호출 X) // 비 로그인 사용자

### 스프링 인터셉터 체인
* HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 인터셉터1 -> 인터셉터2 -> 컨트롤러
* 스프링 인터셉터는 체인으로 구성되는데, 중간에 인터셉터를 자유롭게 추가할 수 있다. 
  예를 들어서 로그를 남기는 인터셉터를 먼저 적용하고, 그 다음에 로그인 여부를 체크하는 인터셉터를 만들 수 있다

### 스프링 인터셉터 호출 흐름
* preHandle : 컨트롤러 호출 전에 호출된다. (더 정확히는 핸들러 어댑터 호출 전에 호출된다.)
* preHandle 의 응답값이 true 이면 다음으로 진행하고, false 이면 더는 진행하지 않는다. false 인 
  경우 나머지 인터셉터는 물론이고, 핸들러 어댑터도 호출되지 않는다.
* postHandle : 컨트롤러 호출 후에 호출된다. (더 정확히는 핸들러 어댑터 호출 후에 호출된다.)
* afterCompletion : 뷰가 렌더링 된 이후에 호출된다.

### 스프링 인터셉터 예외 상황
* preHandle : 컨트롤러 호출 전에 호출된다.
* postHandle : 컨트롤러에서 예외가 발생하면 postHandle 은 호출되지 않는다.
* afterCompletion : afterCompletion 은 항상 호출된다. 이 경우 예외( ex )를 파라미터로 받아서 어떤 예외가 발생했는지 로그로 출력할 수 있다.
  * 예외가 발생하면 postHandle() 는 호출되지 않으므로 예외와 무관하게 공통 처리를 하려면 afterCompletion() 을 사용해야 한다.
  * 예외가 발생하면 afterCompletion() 에 예외 정보( ex )를 포함해서 호출된다.

### 필터 vs 인터셉터
* 서블릿 필터의 경우 단순하게 doFilter() 하나만 제공된다. 인터셉터는 컨트롤러 호출 전( preHandle ), 호출 후( postHandle ), 요청 완료 이후( afterCompletion )와 같이 단계적으로 잘 세분화 되어 있다.
* 서블릿 필터의 경우 단순히 request , response 만 제공했지만, 인터셉터는 어떤 컨트롤러( handler )가 호출되는지 호출 정보도 받을 수 있다. 그리고 어떤 modelAndView 가 반환되는지 응답 정보도 받을 수 있다.

* 스프링 MVC를 사용하고, 특별히 필터를 꼭 사용해야 하는 상황이 아니라면 인터셉터를 사용하는 것이 더 편리하다.

## HandlerMethod
* 핸들러 정보는 어떤 핸들러 매핑을 사용하는가에 따라 달라진다. 
* 스프링을 사용하면 일반적으로 @Controller, @RequestMapping 을 활용한 핸들러 매핑을 사용하는데, 이 경우 핸들러 정보로 HandlerMethod 가 넘어온다.

## ResourceHttpRequestHandler
* @Controller 가 아니라 /resources/static 와 같은 정적 리소스가 호출 되는 경우
* ResourceHttpRequestHandler 가 핸들러 정보로 넘어오기 때문에 타입에 따라서 처리가 필요하다.

## postHandle, afterCompletion
* 종료 로그를 postHandle 이 아니라 afterCompletion 에서 실행한 이유는, 예외가 발생한 경우 postHandle가 호출되지 않기 때문이다. 
* afterCompletion 은 예외가 발생해도 호출 되는 것을 보장한다.

## addInterceptors()
* registry.addInterceptor(new LogInterceptor()) : 인터셉터를 등록한다.
* order(1) : 인터셉터의 호출 순서를 지정한다. 낮을 수록 먼저 호출된다.
* addPathPatterns("/**") : 인터셉터를 적용할 URL 패턴을 지정한다.
* excludePathPatterns("/css/**", "/*.ico", "/error") : 인터셉터에서 제외할 패턴을 지정한다

## 스프링의 URL 경로
* 스프링이 제공하는 URL 경로는 서블릿 기술이 제공하는 URL 경로와 완전히 다르다. 더욱 자세하고, 세밀하게 설정할 수 있다.
