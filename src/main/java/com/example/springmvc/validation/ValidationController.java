package com.example.springmvc.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ValidationController {

    @PostMapping("/valid")
    public ResponseEntity<Void> valid(@ModelAttribute Item item, BindingResult bindingResult) {
        if(!StringUtils.hasText(item.getName())){
            bindingResult.addError(new FieldError("item", "name", "상품 이름은 필수입 니다."));
        }

        if (item.getQuantity() == null || item.getQuantity() >= 10000) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/validV2")
    public ResponseEntity<Void> validV2(@ModelAttribute Item item, BindingResult bindingResult) {
        if(!StringUtils.hasText(item.getName())){
            bindingResult.rejectValue("itemName", "required");
        }

        if (item.getQuantity() == null || item.getQuantity() >= 10000) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}
