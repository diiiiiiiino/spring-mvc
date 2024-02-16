### 스프링 메세지 소스 설정
* 스프링이 제공하는 MessageSource 를 스프링 빈으로 등록하면 되는데, MessageSource 는 인터페이스이다. 
* 따라서 구현체인 ResourceBundleMessageSource 를 스프링 빈으로 등록하면 된다.

* basenames : 설정 파일의 이름을 지정한다.
  * messages 로 지정하면 messages.properties 파일을 읽어서 사용한다.
  * 추가로 국제화 기능을 적용하려면 messages_en.properties , messages_ko.properties 와 같
  이 파일명 마지막에 언어 정보를 주면된다. 만약 찾을 수 있는 국제화 파일이 없으면 messages.properties (언어정보가 없는 파일명)를 기본으로 사용한다.
  * 파일의 위치는 /resources/messages.properties 에 두면 된다.
  * 여러 파일을 한번에 지정할 수 있다. 여기서는 messages , errors 둘을 지정했다.
* defaultEncoding : 인코딩 정보를 지정한다. utf-8 을 사용하면 된다.

### 스프링 부트 메세지 소스 설정
* 스프링 부트를 사용하면 스프링 부트가 MessageSource 를 자동으로 스프링 빈으로 등록한다.
* application.yaml에 아래와 같이 적용할 수 있다.
  * ```spring.messages.basename: messages,config.i18n.messages```
* 스프링 부트 메세지 소스 기본 값
  * ```spring.messages.basename=messages```
* MessageSource 를 스프링 빈으로 등록하지 않고, 스프링 부트와 관련된 별도의 설정을 하지 않으면 messages라는 이름으로 기본 등록된다. 
* 따라서 messages_en.properties, messages_ko.properties, messages.properties 파일만 등록하면 자동으로 인식된다.

## 국제화 파일 선택
* locale 정보를 기반으로 국제화 파일을 선택한다.
* Locale이 en_US 의 경우 messages_en_US messages_en messages 순서로 찾는다.
* Locale 에 맞추어 구체적인 것이 있으면 구체적인 것을 찾고, 없으면 디폴트를 찾는다고 이해하면 된다.

* Locale 정보가 없는 경우
  * Locale.getDefault() 을 호출해서 시스템의 기본 로케일을 사용합니다.
  * 예) locale = null 인 경우 시스템 기본 locale 이 ko_KR 이므로 messages_ko.properties 조회 시도
  * 조회 실패 messages.properties 조회

## 스프링의 국제화 메시지 선택
* 스프링도 Locale 정보를 알아야 언어를 선택할 수 있는데, 스프링은 언어 선택시 기본으로 AcceptLanguage 헤더의 값을 사용한다.

## LocaleResolver
* 스프링은 Locale 선택 방식을 변경할 수 있도록 LocaleResolver 라는 인터페이스를 제공하는데, 
* 스프링 부트는 기본으로 Accept-Language 를 활용하는 AcceptHeaderLocaleResolver 를 사용한다.

## LocaleResolver 변경
* 만약 Locale 선택 방식을 변경하려면 LocaleResolver 의 구현체를 변경해서 쿠키나 세션 기반의 Locale 선택 기능을 사용할 수 있다. 
* 예를 들어서 고객이 직접 Locale 을 선택하도록 하는 것이다.