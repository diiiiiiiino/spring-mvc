## MVC 패턴 - 등장 배경
- 너무 많은 역할
  - 하나의 서블릿이나 JSP만으로 비즈니스 로직과 뷰 렌더링까지 처리시 많은 역할을 하게 되어 유지보수에 어려움 <br>
- 변경의 라이프 사이클
  - UI와 비즈니스 로직 수정은 개별적으로 발생하며 변경이 다른 부분을 하나의 코드에서 관리하면 유지보수가 좋지 않다
- 기능 특화
  - JSP 같은 뷰 템플릿은 화면 렌더링에 최적화 되어 있어 해당 기능만 담당하는 것이 효과적

## MVC 패턴 - 한계
- 포워드 중복
  - View로 이동하는 코드 중복 호출
  - ViewPath 중복
  - 파라미터에 HttpServletRequest request, HttpServletResponse response 둘 중 사용하지 않는 경우도 있음 <br>
    테스트 코드를 작성하기도 어려움
  - 기능이 복잡해질 수록 컨트롤러에서 공통으로 처리해야 하는 부분이 많아진다

## redirect
- 클라이언트에 응답이 나갔다가 클라이언트가 redirect 경로로 다시 요청

## forward
- 서버 내부에서 발생하는 호출 (클라이언트가 인지하지 못함)