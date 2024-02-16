## 스프링 부트가 자동 등록하는 핸들러 매핑과 핸들러 어댑터
- HandlerMapping
  - RequestMappingHandlerMapping : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
  - BeanNameUrlHandlerMapping : 스프링 빈의 이름으로 핸들러를 찾는다.

- HandlerAdapter
  - RequestMappingHandlerAdapter : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
  - HttpRequestHandlerAdapter : HttpRequestHandler 처리
  - SimpleControllerHandlerAdapter : Controller 인터페이스(애노테이션X, 과거에 사용) 처리 

## OldController 실행 순서
1. 핸들러 매핑으로 핸들러 조회
   1. HandlerMapping 을 순서대로 실행해서, 핸들러를 찾는다.
   2. 이 경우 빈 이름으로 핸들러를 찾아야 하기 때문에 이름 그대로 빈 이름으로 핸들러를 찾아주는
      BeanNameUrlHandlerMapping 가 실행에 성공하고 핸들러인 OldController 를 반환한다.
2. 핸들러 어댑터 조회
   1. HandlerAdapter 의 supports() 를 순서대로 호출한다.
   2. SimpleControllerHandlerAdapter 가 Controller 인터페이스를 지원하므로 대상이 된다.
3. 핸들러 어댑터 실행
   1. 디스패처 서블릿이 조회한 SimpleControllerHandlerAdapter 를 실행하면서 핸들러 정보도 함께 넘겨준
      다.
   2. SimpleControllerHandlerAdapter 는 핸들러인 OldController 를 내부에서 실행하고, 그 결과를 반
      환한다
   
## 뷰 리졸버 - InternalResourceViewResolver
- 스프링 부트는 InternalResourceViewResolver를 자동으로 등록하는데 이때, applicat.yaml에 등록한 <br>
  spring.mvc.view.prefix, spring.mvc.view.suffix 설정정보를 사용해서 등록한다.
- 권장사항은 아니지만 전체 경로를 주어도 동작한다. new ModelAndView("/WEB-INF/views/new-form.jsp");

## 스프링 부트가 자동 등록하는 뷰 리졸버
- BeanNameViewResolver :빈 이름으로 뷰를 찾아서 반환한다. (엑셀 파일 생성 기능에 사용)
- InternalResourceViewResolver : JSP를 처리할 수 있는 뷰를 반환한다.
- 실행 순서
    1. 핸들러 어댑터 호출
       - 핸들러 어댑터를 통해 new-form이라는 논리 뷰 이름을 획득한다.
    2. ViewResolver 호출
       - new-form이라는 뷰 이름으로 viewResolver를 순서대로 호출한다.
       - BeanNameViewResolver는 new-form이라는 이름의 스프링 빈으로 등록된 뷰를 찾아야 하는데 없다.
       - InternalResourceViewResolver가 호출된다.
    3. InternalResourceViewResolver
       - InternalResourceView를 반환한다.
    4. InternalResourceView
       - JSP처럼 forward()를 호출해서 처리할 수 있는 경우에 사용한다.
    5. view.render()
       - view.render()호출되고 InternalResourceView는 forward()를 사용해서 JSP를 실행한다.
* InternalResourceViewResolver는 만약 JSTL 라이브러리가 있으면 InternalResourceView를 상속받은 JstlView를 반환한다. <br>
  JstlView는 JSTL 태그 사용시 약간의 부가 기능이 추가된다.
* 다른 뷰는 실제 뷰를 렌더링하지만, JSP의 경우 forward() 통해서 해당 JSP로 이동해야 렌더링이 된다. <br>
  JSP를 제외한 나머지 뷰 템플릿들은 forward() 과정 없이 바로 렌더링 된다.
* Thymeleaf 뷰 템플릿을 사용하면 ThymeleafViewResolver 를 등록해야 한다. 최근에는 라이브러리만 추가하면 스프링 부트가 이런 작업도 모두 자동화해준다.

* @Controller :
  - 스프링이 자동으로 스프링 빈으로 등록한다. (내부에 @Component 애노테이션이 있어서 컴포넌트 스캔의 대상이 됨)
  - 스프링 MVC에서 애노테이션 기반 컨트롤러로 인식한다.
* @RequestMapping : 요청 정보를 매핑한다. 해당 URL이 호출되면 이 메서드가 호출된다. 애노테이션을 기반으로 동작하기 때문에, 메서드의 이름은 임의로 지으면 된다.
* ModelAndView : 모델과 뷰 정보를 담아서 반환하면 된다
* @RequestParam 사용
  - 스프링은 HTTP 요청 파라미터를 @RequestParam 으로 받을 수 있다.
    @RequestParam("username") 은 request.getParameter("username") 와 거의 같은 코드라 생각하면 된다.
    물론 GET 쿼리 파라미터, POST Form 방식을 모두 지원한다.