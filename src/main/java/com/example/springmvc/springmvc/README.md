## DispatcherServlet
- 부모 클래스로부터 HttpServlet을 상속 받으며 서블릿으로 동작한다.
  - DispatcherServlet -> FrameworkServlet -> HttpServletBean -> HttpServlet
- 스프링 부트는 DispatcherServlet을 자동으로 등록하면서 모든 경로(urlPatterns="/")에 대해서 매핑한다
  - 더 자세한 경로가 우선순위가 높다.
- 요청흐름
  - HttpServlet#service() 호출
  - 스프링 MVC는 DispatcherServlet의 부모인 FrameworkServlet에서 service()를 오버라이드 함
  - FrameworkServlet#service()를 시작으로 여러 메서드가 호출되면서 DispatcherServlet#doDispatch()가 호출된다.

- 동작순서
  1. 핸들러 조회 : 핸들러 매핑을 통해 요청 URL에 매핑된 핸들러를 조회한다
  2. 핸들러 어댑터 조회 : 핸들러를 실행할 수 있는 핸들러 어댑터를 조회한다
  3. 핸들러 어댑터 실행 : 핸들러 어댑터를 실행한다
  4. 핸들러 실행 : 핸들러 어댑터가 실제 핸들러를 실행한다
  5. ModelAndView 반환 : 핸들러 어댑터는 핸들러가 반환하는 정보를 ModelAndView로 변환해서 반환한다
  6. viewResolver 호출 : 뷰 리졸버를 찾고 실행한다
     - JSP의 경우 : InternalResourceViewResolver가 자동 등록 되고, 사용된다
  7. View 반환 : 뷰 리졸버는 뷰의 논리 이름을 물리 이름으로 바꾸고, 렌더링 역할을 담당하는 뷰 객체를 반환한다.
     - JSP의 경우 InternalResourceView(JstlView)를 반환하는데, 내부에 forward() 로직이 있다
  8. 뷰 렌더링 : 뷰를 통해서 뷰를 렌더링 한다.