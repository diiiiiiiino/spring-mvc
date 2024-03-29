## 서블릿 필터
### 필터 흐름
* HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 컨트롤러
* 필터를 적용하면 필터가 호출 된 다음에 서블릿이 호출된다.
  그래서 모든 고객의 요청 로그를 남기는 요구사항이 있다면 필터를 사용하면 된다. 
  참고로 필터는 특정 URL 패턴에 적용할 수 있다. /* 이라고 하면 모든 요청에 필터가 적용된다.
  참고로 스프링을 사용하는 경우 여기서 말하는 서블릿은 스프링의 디스패처 서블릿으로 생각하면 된다.

### 필터 제한
* HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 컨트롤러 //로그인 사용자
* HTTP 요청 -> WAS -> 필터(적절하지 않은 요청이라 판단, 서블릿 호출X) //비 로그인 사용자

### 필터 체인
* HTTP 요청 -> WAS -> 필터1 -> 필터2 -> 필터3 -> 서블릿 -> 컨트롤러
* 필터는 체인으로 구성되는데, 중간에 필터를 자유롭게 추가할 수 있다.

### 필터 인터페이스
필터 인터페이스를 구현하고 등록하면 서블릿 컨테이너가 필터를 싱글톤 객체로 생성하고, 관리한다.
* init(): 필터 초기화 메서드, 서블릿 컨테이너가 생성될 때 호출된다.
* doFilter(): 고객의 요청이 올 때 마다 해당 메서드가 호출된다. 필터의 로직을 구현하면 된다.
* destroy(): 필터 종료 메서드, 서블릿 컨테이너가 종료될 때 호출된다.

### 필터 부가 기능
* 필터에는 다음에 설명할 스프링 인터셉터는 제공하지 않는, 아주 강력한 기능이 있는데
  chain.doFilter(request, response); 를 호출해서 다음 필터 또는 서블릿을 호출할 때 request,
  response를 다른 객체로 바꿀 수 있다. 
* ServletRequest, ServletResponse를 구현한 다른 객체를 만들어서 넘기면 해당 객체가 다음 필터 또는 서블릿에서 사용된다.

### FilterRegistrationBean
* 스프링 부트 사용시 필토를 등록하는 객체
* setFilter(new LogFilter()) : 등록할 필터를 지정한다.
* setOrder(1) : 필터는 체인으로 동작한다. 따라서 순서가 필요하다. 낮을 수록 먼저 동작한다.
* addUrlPatterns("/*") : 필터를 적용할 URL 패턴을 지정한다. 한번에 여러 패턴을 지정할 수 있다.
* @ServletComponentScan @WebFilter(filterName = "logFilter", urlPatterns = "/*") 로 
  필터 등록이 가능하지만 필터 순서 조절이 안된다. 따라서 FilterRegistrationBean 을 사용하자.