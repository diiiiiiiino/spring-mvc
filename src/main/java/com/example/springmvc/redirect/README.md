## POST, Redirect GET
* 예를 들어 상품 등록 폼 화면에서 등록 완료 후 새로고침을 누르면 <br>
  똑같은 상품이 또 등록되는 문제가 발생한다.
* 이런 문제를 해결하기 위해 상품 등록 후 상품 상세 페이지로 리다이렉트 처리를 해줘야한다.

## RedirectAttributes
* RedirectAttributes 를 사용하면 URL 인코딩도 해주고, pathVariable , 쿼리 파라미터까지 처리해준다.
  * redirect:/basic/items/{itemId}
  * pathVariable 바인딩: {itemId}
  * 나머지는 쿼리 파라미터로 처리: ?status=true