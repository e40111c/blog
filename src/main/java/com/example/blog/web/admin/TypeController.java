package com.example.blog.web.admin;


import com.example.blog.po.Type;
import com.example.blog.service.TypeService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    //顯示資料庫儲存的type，siz=數量，Pagedefault載入時預設加入)
    public String types(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable, Model model){
        model.addAttribute("page",typeService.listType(pageable));
        return "admin/types";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("type",typeService.getType(id));
        return "admin/type-input";
    }

    @GetMapping("/types/input")
    public String input(Model model) {
        model.addAttribute("type",new Type());
        return "admin/type-input";
    }

    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result , RedirectAttributes attributes) {
        Type sameNameError = typeService.getTypeByName(type.getName());
        if(sameNameError != null) {
            result.rejectValue("name","nameError","分類名稱重複");
        }
        if(result.hasErrors()) {
            return "admin/type-input";
        }
        //第一個string要跟type的name一樣
        Type saveType = typeService.saveType(type);
        if(saveType == null) {
            attributes.addFlashAttribute("message","新增失敗");
        } else  {
            attributes.addFlashAttribute("message","新增成功");
        }
        return "redirect:/admin/types";
    }

    @PostMapping("/types/{id}")
    public String editpost(@Valid Type type, BindingResult result ,@PathVariable Long id, RedirectAttributes attributes) throws NotFoundException {
        Type sameNameError = typeService.getTypeByName(type.getName());
        if(sameNameError != null) {
            result.rejectValue("name","nameError","分類名稱重複");
        }
        if(result.hasErrors()) {
            return "admin/type-input";
        }
        //第一個string要跟type的name一樣
        Type saveType = typeService.updateType(id,type);
        if(saveType == null) {
            attributes.addFlashAttribute("message","更新失敗");
        } else  {
            attributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message","刪除成功");
        return "redirect:/admin/types";
    }


}
