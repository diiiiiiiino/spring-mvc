## Bean Validation
* 특정한 구현체가 아니라 Bean Validation 2.0(JSR-380)이라는 기술 표준
* 검증 애노테이션과 여러 인터페이스의 모음

## Jakarta Bean Validation
* jakarta.validation-api : Bean Validation 인터페이스
* hibernate-validator 구현체

## 검증 어노테이션
* @NotBlank : 빈값 + 공백만 있는 경우를 허용하지 않는다.
* @NotNull : null 을 허용하지 않는다.
* @Range(min = 1000, max = 1000000) : 범위 안의 값이어야 한다.
* @Max(9999) : 최대 9999까지만 허용한다

## javax.validation.constraints / org.hibernate.validator.constraints
* javax.validation 으로 시작하면 특정 구현에 관계없이 제공되는 표준 인터페이스이고,
* org.hibernate.validator 로 시작하면 하이버네이트 validator 구현체를 사용할 때만 제공되는 검증 기능이다. 
* 실무에서 대부분 하이버네이트 validator를 사용하므로 자유롭게 사용해도 된다

## 스프링 MVC는 어떻게 Bean Validator를 사용?
* 스프링 부트가 spring-boot-starter-validation 라이브러리를 넣으면 자동으로 Bean Validator를 인지하고 스프링에 통합한다.

## 스프링 부트는 자동으로 글로벌 Validator로 등록한다.
* LocalValidatorFactoryBean 을 글로벌 Validator로 등록한다. 
* 이 Validator는 @NotNull 같은 애노테이션을 보고 검증을 수행한다. 
* 이렇게 글로벌 Validator가 적용되어 있기 때문에, @Valid , @Validated 만 적용하면 된다.
* 검증 오류가 발생하면, FieldError , ObjectError 를 생성해서 BindingResult 에 담아준다.
* 주의
  * 직접 글로벌 Validator를 직접 등록하면 스프링 부트는 Bean Validator를 글로벌 Validator 로 등록 하지 않는다. 
  * 따라서 애노테이션 기반의 빈 검증기가 동작하지 않는다. 다음 부분은 제거하자.

## @Validated @Valid
* javax.validation.@Valid 를 사용하려면 build.gradle 의존관계 추가가 필요하다. (이전에 추가했다.)
* implementation 'org.springframework.boot:spring-boot-starter-validation'
* @Validated 는 스프링 전용 검증 애노테이션이고, @Valid 는 자바 표준 검증 애노테이션이다. 
* 둘중 아무거나 사용해도 동일하게 작동하지만, @Validated 는 내부에 groups 라는 기능을 포함하고 있다.

## 검증 순서
1. @ModelAttribute 각각의 필드에 타입 변환 시도
   1. 성공하면 다음으로
   2. 실패하면 typeMismatch 로 FieldError 추가
2. Validator 적용

## 바인딩에 성공한 필드만 Bean Validation 적용
* BeanValidator는 바인딩에 실패한 필드는 BeanValidation을 적용하지 않는다.
* 생각해보면 타입 변환에 성공해서 바인딩에 성공한 필드여야 BeanValidation 적용이 의미 있다.
* (일단 모델 객체에 바인딩 받는 값이 정상으로 들어와야 검증도 의미가 있다.)
* @ModelAttribute 각각의 필드 타입 변환시도 변환에 성공한 필드만 BeanValidation 적용

## Bean Validation - 에러 코드
* NotBlank 라는 오류 코드를 기반으로 MessageCodesResolver 를 통해 다양한 메시지 코드가 순서대로 생성된다
* @NotBlank
  * NotBlank.item.itemName
  * NotBlank.itemName
  * NotBlank.java.lang.String
  * NotBlank

## BeanValidation 메시지 찾는 순서
1. 생성된 메시지 코드 순서대로 messageSource 에서 메시지 찾기
2. 애노테이션의 message 속성 사용 @NotBlank(message = "공백! {0}")
3. 라이브러리가 제공하는 기본 값 사용 공백일 수 없습니다

## Bean Validation - 오브젝트 오류
* @ScriptAssert 을 억지로 사용하는 것 보다는 다음과 같이 오브젝트 오류 관련 부분만 직접 자바 코드로 작성하는 것을 권장

## HTTP 요청 검증 유의 사항
* HTTP 요청은 언제든지 악의적으로 변경해서 요청할 수 있으므로 서버에서 항상 검증 해야 한다. 
* 예를 들어서 HTTP 요청을 변경해서 item 의 id 값을 삭제하고 요청할 수도 있다. 따라서 최종 검증은 서버에서 진행하는 것이 안전한다.

## Bean Validation - HTTP 메시지 컨버터
* @Valid , @Validated 는 HttpMessageConverter ( @RequestBody )에도 적용할 수 있다.
* HttpMessageConverter 단계에서 실패하면 예외가 발생한다.

## @ModelAttribute vs @RequestBody
* @ModelAttribute 는 필드 단위로 정교하게 바인딩이 적용된다. 특정 필드가 바인딩 되지 않아도 나머지 필드는 정상 바인딩 되고, Validator를 사용한 검증도 적용할 수 있다.
* @RequestBody 는 HttpMessageConverter 단계에서 JSON 데이터를 객체로 변경하지 못하면 이후 단계 자체가 진행되지 않고 예외가 발생한다. 컨트롤러도 호출되지 않고, Validator도 적용할 수 없다.