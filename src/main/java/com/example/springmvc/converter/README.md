## 스프링의 타입 변환 적용
* 스프링 MVC 요청 파라미터
  * @RequestParam , @ModelAttribute , @PathVariable
* @Value 등으로 YML 정보 읽기
* XML에 넣은 스프링 빈 정보를 변환
* 뷰를 렌더링 할 때

## 타입 컨버터 - Converter
* 스프링이 중간에 타입 변환기를 사용해서 타입을 String Integer 로 변환해주었기 때문에 개발자는 편리하게 해당 타입을 바로 받을 수 있다
### 스프링 타입 컨버터
* Converter 기본 타입 컨버터
* ConverterFactory 전체 클래스 계층 구조가 필요할 때
* GenericConverter 정교한 구현, 대상 필드의 애노테이션 정보 사용 가능
* ConditionalGenericConverter 특정 조건이 참인 경우에만 실행
* 스프링은 문자, 숫자, 불린, Enum등 일반적인 타입에 대한 대부분의 컨버터를 기본으로 제공한다. IDE에서
  Converter , ConverterFactory , GenericConverter 의 구현체를 찾아보면 수 많은 컨버터를 확인할
  수 있다.
* 컨버터를 추가하면 추가한 컨버터가 기본 컨버터 보다 높은 우선순위를 가진다

## 컨버전 서비스 - ConversionService
* 이렇게 타입 컨버터를 하나하나 직접 찾아서 타입 변환에 사용하는 것은 매우 불편하다. 그래서 스프링은 개별 컨버터를
  모아두고 그것들을 묶어서 편리하게 사용할 수 있는 기능을 제공하는데, 이것이 바로 컨버전 서비스
  ( ConversionService )이다.
### 등록과 사용 분리
* 컨버터를 등록할 때는 StringToIntegerConverter 같은 타입 컨버터를 명확하게 알아야 한다. 반면에 컨버터를
  사용하는 입장에서는 타입 컨버터를 전혀 몰라도 된다. 타입 컨버터들은 모두 컨버전 서비스 내부에 숨어서 제공된다.
  따라서 타입을 변환을 원하는 사용자는 컨버전 서비스 인터페이스에만 의존하면 된다. 물론 컨버전 서비스를 등록하는
  부분과 사용하는 부분을 분리하고 의존관계 주입을 사용해야 한다

## 인터페이스 분리 원칙 - ISP(Interface Segregation Principle)
인터페이스 분리 원칙은 클라이언트가 자신이 이용하지 않는 메서드에 의존하지 않아야 한다.
DefaultConversionService 는 다음 두 인터페이스를 구현했다.
* ConversionService : 컨버터 사용에 초점
* ConverterRegistry : 컨버터 등록에 초점
* 이렇게 인터페이스를 분리하면 컨버터를 사용하는 클라이언트와 컨버터를 등록하고 관리하는 클라이언트의 관심사를
  명확하게 분리할 수 있다. 특히 컨버터를 사용하는 클라이언트는 ConversionService 만 의존하면 되므로, 컨버터
  를 어떻게 등록하고 관리하는지는 전혀 몰라도 된다. 결과적으로 컨버터를 사용하는 클라이언트는 꼭 필요한 메서드만
  알게된다. 이렇게 인터페이스를 분리하는 것을 ISP 라 한다.

## 컨버터 처리 과정
* @RequestParam 은 @RequestParam 을 처리하는 ArgumentResolver 인
  RequestParamMethodArgumentResolver 에서 ConversionService 를 사용해서 타입을 변환한다. 부모 클
  래스와 다양한 외부 클래스를 호출하는 등 복잡한 내부 과정을 거치기 때문에 대략 이렇게 처리되는 것으로 이해해도 충
  분하다. 만약 더 깊이있게 확인하고 싶으면 IpPortConverter 에 디버그 브레이크 포인트를 걸어서 확인해보자.