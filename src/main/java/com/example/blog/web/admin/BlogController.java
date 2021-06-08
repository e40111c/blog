package com.example.blog.web.admin;


import com.example.blog.po.Blog;
import com.example.blog.po.User;
import com.example.blog.service.BlogService;
import com.example.blog.service.TagService;
import com.example.blog.service.TypeService;
import com.example.blog.vo.BlogQuery;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private final static String INPUT = "admin/blog-input";
    private final static String LIST = "admin/admin";
    private final static String REDIRECT_LIST = "redirect:/admin/admin";


    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/admin")
    public String admin(@PageableDefault(size = 4,sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable
            , BlogQuery blog, Model model)  {
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return LIST ;
    }

    @PostMapping("/admin/search")
    public String search(@PageableDefault(size = 2,sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable
            , BlogQuery blog, Model model)  {
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return "admin/admin :: adminList";
    }

    @GetMapping("/admin/input")
    public String input(Model model) {
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        model.addAttribute("blog",new Blog());
        return INPUT;
    }


    @GetMapping("/admin/{id}/input")
    public String editinput(@PathVariable Long id, Model model) {
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }

    @PostMapping("/admin")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) throws NotFoundException {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog postBlog;
        if (blog.getId() == null) {
            postBlog = blogService.saveBlog(blog);
        } else {
            postBlog = blogService.updateBlog(blog.getId(),blog);
        }
        if (postBlog == null ) {
            attributes.addFlashAttribute("message", "Fail!");
        } else {
            attributes.addFlashAttribute("message", "Succeed!");
        }
        return REDIRECT_LIST;
    }

    @GetMapping("/admin/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "刪除成功");
        return REDIRECT_LIST;
    }


}
