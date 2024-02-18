## Formatter
* Converter 는 입력과 출력 타입에 제한이 없는, 범용 타입 변환 기능을 제공한다.
  이번에는 일반적인 웹 애플리케이션 환경을 생각해보자. 불린 타입을 숫자로 바꾸는 것 같은 범용 기능 보다는 개발자
  입장에서는 문자를 다른 타입으로 변환하거나, 다른 타입을 문자로 변환하는 상황이 대부분이다.
  앞서 살펴본 예제들을 떠올려 보면 문자를 다른 객체로 변환하거나 객체를 문자로 변환하는 일이 대부분이다.
*  포맷터는 컨버터의 특별한 버전으로 이해하면 된다
## Converter vs Formatter
* Converter 는 범용(객체 객체)
* Formatter 는 문자에 특화(객체 문자, 문자 객체) + 현지화(Locale)
  * Converter 의 특별한 버전

## 포맷터를 지원하는 컨버전 서비스
* 컨버전 서비스에는 컨버터만 등록할 수 있고, 포맷터를 등록할 수 는 없다. 그런데 생각해보면 포맷터는 객체 문자, 문
  자 객체로 변환하는 특별한 컨버터일 뿐이다.
  포맷터를 지원하는 컨버전 서비스를 사용하면 컨버전 서비스에 포맷터를 추가할 수 있다. 내부에서 어댑터 패턴을 사용
  해서 Formatter 가 Converter 처럼 동작하도록 지원한다.
## FormattingConversionService
* 포맷터를 지원하는 컨버전 서비스이다
* DefaultFormattingConversionService 는 FormattingConversionService 에 기본적인 통화, 숫자 관
  련 몇가지 기본 포맷터를 추가해서 제공한다.
### DefaultFormattingConversionService 상속 관계
* FormattingConversionService 는 ConversionService 관련 기능을 상속받기 때문에 결과적으로 컨버터도 
  포맷터도 모두 등록할 수 있다. 그리고 사용할 때는 ConversionService 가 제공하는 convert 를 사용하면 된다.
* 추가로 스프링 부트는 DefaultFormattingConversionService 를 상속 받은 WebConversionService 를 
  내부에서 사용한다.
### 우선순위
* 우선순위는 컨버터가 우선하므로 포맷터가 적용되지 않고, 컨버터가 적용된다

## 스프링이 제공하는 기본 포맷터
* @NumberFormat : 숫자 관련 형식 지정 포맷터 사용, NumberFormatAnnotationFormatterFactory
* @DateTimeFormat : 날짜 관련 형식 지정 포맷터 사용, Jsr310DateTimeFormatAnnotationFormatterFactory

## HttpMessageConverter
* 메시지 컨버터( HttpMessageConverter )에는 컨버전 서비스가 적용되지 않는다.
  특히 객체를 JSON으로 변환할 때 메시지 컨버터를 사용하면서 이 부분을 많이 오해하는데,
  HttpMessageConverter 의 역할은 HTTP 메시지 바디의 내용을 객체로 변환하거나 객체를 HTTP 메시지 바디에
  입력하는 것이다. 예를 들어서 JSON을 객체로 변환하는 메시지 컨버터는 내부에서 Jackson 같은 라이브러리를 사용
  한다. 객체를 JSON으로 변환한다면 그 결과는 이 라이브러리에 달린 것이다. 따라서 JSON 결과로 만들어지는 숫자나
  날짜 포맷을 변경하고 싶으면 해당 라이브러리가 제공하는 설정을 통해서 포맷을 지정해야 한다. 결과적으로 이것은 컨
  버전 서비스와 전혀 관계가 없다.
* 컨버전 서비스는 @RequestParam , @ModelAttribute , @PathVariable , 뷰 템플릿 등에서 사용할 수 있다