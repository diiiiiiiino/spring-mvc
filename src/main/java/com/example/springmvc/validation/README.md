## 클라이언트 검증, 서버 검증
* 클라이언트 검증은 조작할 수 있으므로 보안에 취약하다.
* 서버만으로 검증하면, 즉각적인 고객 사용성이 부족해진다.
* 둘을 적절히 섞어서 사용하되, 최종적으로 서버 검증은 필수
* API 방식을 사용하면 API 스펙을 잘 정의해서 검증 오류를 API 응답 결과에 잘 남겨주어야 함

## BindingResult
* BindingResult bindingResult 파라미터의 위치는 @ModelAttribute Item item 다음에 와야 한다.
* 스프링이 제공하는 검증 오류를 보관하는 객체이다. 검증 오류가 발생하면 여기에 보관하면 된다.
* @ModelAttribute에 바인딩 시 타입 오류가 발생하면?
  * BindingResult 가 없으면 400 오류가 발생하면서 컨트롤러가 호출되지 않고, 오류 페이지로 이동한다.
  * BindingResult 가 있으면 오류 정보( FieldError )를 BindingResult 에 담아서 컨트롤러를 정상 호출한다.

## BindingResult에 검증 오류를 적용하는 3가지 방법
* @ModelAttribute 의 객체에 타입 오류 등으로 바인딩이 실패하는 경우 스프링이 FieldError 생성해서 BindingResult 에 넣어준다.
* 개발자가 직접 넣어준다.
*  Validator 사용 이것은 뒤에서 설명

## FieldError
* 필드에 오류가 있으면 FieldError 객체를 생성해서 bindingResult 에 담아두면 된다.
  * objectName : @ModelAttribute 이름
  * field : 오류가 발생한 필드 이름
  * rejectedValue : 사용자가 입력한 값(거절된 값), 오류 발생시 사용자 입력 값을 저장하는 필드
  * bindingFailure : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값
  * codes : 메시지 코드
  * arguments : 메시지에서 사용하는 인자
  * defaultMessage : 기본 오류 메시지

## ObjectError
* 특정 필드를 넘어서는 오류가 있으면 ObjectError 객체를 생성해서 bindingResult 에 담아두면 된다.
  * objectName : @ModelAttribute 의 이름
  * defaultMessage : 오류 기본 메시지

## BindingResult와 Errors
* org.springframework.validation.Errors
* org.springframework.validation.BindingResult
* BindingResult 는 인터페이스이고, Errors 인터페이스를 상속받고 있다.
* 실제 넘어오는 구현체는 BeanPropertyBindingResult 라는 것인데, 둘다 구현하고 있으므로 BindingResult 대신에 Errors 를 사용해도 된다. 
* Errors 인터페이스는 단순한 오류 저장과 조회 기능을 제공한다.
* BindingResult 는 여기에 더해서 추가적인 기능들을 제공한다. 
* addError() 도 BindingResult 가 제공하므로 여기서는 BindingResult를 사용하자. 주로 관례상 BindingResult 를 많이 사용한다

## 스프링의 바인딩 오류 처리
* 타입 오류로 바인딩에 실패하면 스프링은 FieldError 를 생성하면서 사용자가 입력한 값을 넣어둔다. 그리고 해당 오류를 BindingResult 에 담아서 컨트롤러를 호출한다. 
* 따라서 타입 오류 같은 바인딩 실패시에도 사용자의 오류 메시지를 정상 출력할 수 있다

## BindingResult#rejectValue(), reject()
* rejectValue() , reject() 를 사용하면 FieldError , ObjectError 를 직접 생성하지 않고, 깔끔하게 검증 오류를 다룰 수 있다

## rejectValue()
``` void rejectValue(@Nullable String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage); ```
* field : 오류 필드명
* errorCode : 오류 코드(이 오류 코드는 메시지에 등록된 코드가 아니다. 뒤에서 설명할 messageResolver를 위한 오류 코드이다.)
* errorArgs : 오류 메시지에서 {0} 을 치환하기 위한 값
* defaultMessage : 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지

## 축약된 오류 코드
* FieldError() 를 직접 다룰 때는 오류 코드를 range.item.price 와 같이 모두 입력했다. 
* 그런데 rejectValue() 를 사용하고 부터는 오류 코드를 range 로 간단하게 입력했다. 
* 그래도 오류 메시지를 잘 찾아서 출력한다. 
* 이 부분을 이해하려면 MessageCodesResolver 를 이해해야 한다. 왜 이런식으로 오류 코드를 구성하는지 바로 다음에 자세히 알아보자
* ``` 
  errors.properties
  range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
  ``` 
  
## 오류 코드와 메세지 처리
* 가장 좋은 방법은 범용성으로 사용하다가, 세밀하게 작성해야 하는 경우에는 세밀한 내용이 적용되도록 메시지에 단계를 두는 방법이다
* MessageCodesResolver로 이러한 기능을 지원한다
* ``` 
  #Level1
  required.item.itemName: 상품 이름은 필수 입니다.
  #Level2
  required: 필수 값 입니다.
  ```
  
## MessageCodesResolver
* 검증 오류 코드로 메시지 코드들을 생성한다.
* MessageCodesResolver 인터페이스이고 DefaultMessageCodesResolver 는 기본 구현체이다.
* 주로 다음과 함께 사용 ObjectError , FieldError

## DefaultMessageCodesResolver의 기본 메시지 생성 규칙
* 객체 오류
  ```
    객체 오류의 경우 다음 순서로 2가지 생성
    1.: code + "." + object name
    2.: code
    예) 오류 코드: required, object name: item
    1.: required.item
    2.: required
  ```
* 필드 오류
  ```
    필드 오류의 경우 다음 순서로 4가지 메시지 코드 생성
    1.: code + "." + object name + "." + field
    2.: code + "." + field
    3.: code + "." + field type
    4.: code
    예) 오류 코드: typeMismatch, object name "user", field "age", field type: int
    1. "typeMismatch.user.age"
    2. "typeMismatch.age"
    3. "typeMismatch.int"
    4. "typeMismatch"
  ```
* 동작 방식
  * rejectValue() , reject() 는 내부에서 MessageCodesResolver 를 사용한다. 여기에서 메시지 코드들을 생성한다.
  * FieldError , ObjectError 의 생성자를 보면, 오류 코드를 하나가 아니라 여러 오류 코드를 가질 수 있다.
  * MessageCodesResolver 를 통해서 생성된 순서대로 오류 코드를 보관한다.
  * 이 부분을 BindingResult 의 로그를 통해서 확인해보자.
    * codes [range.item.price, range.price, range.java.lang.Integer, range]

* 오류 코드 관리 전략
  * 핵심은 구체적인 것에서! 덜 구체적인 것으로!
  * MessageCodesResolver 는 required.item.itemName 처럼 구체적인 것을 먼저 만들어주고, required 처럼 덜 구체적인 것을 가장 나중에 만든다.
    이렇게 하면 앞서 말한 것 처럼 메시지와 관련된 공통 전략을 편리하게 도입할 수 있다

* 왜 이렇게 복잡하게 사용하는가?
  * 모든 오류 코드에 대해서 메시지를 각각 다 정의하면 개발자 입장에서 관리하기 너무 힘들다.
  * 크게 중요하지 않은 메시지는 범용성 있는 requried 같은 메시지로 끝내고, 정말 중요한 메시지는 꼭 필요할 때 구체
    적으로 적어서 사용하는 방식이 더 효과적이다

* Validator
  * 검증 로직을 별도의 클래스로 분리한다 
  * supports() {} : 해당 검증기를 지원하는 여부 확인(뒤에서 설명)
  * validate(Object target, Errors errors) : 검증 대상 객체와 BindingResult