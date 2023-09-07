 package com.example.demo.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.demo.controller.binding.AddPostForm;
import com.example.demo.controller.exception.ResourceNotFoundException;
import com.example.demo.entity.LikeRecord;
import com.example.demo.entity.Comment;
import com.example.demo.entity.LikeId;
import com.example.demo.entity.Post;
import com.example.demo.entity.Userlog;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.LikeCRUDRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.business.LoggedInUser;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;

@Controller
@RequestMapping("/forum")
public class ForumController {
    @Autowired
    private LoggedInUser loggedInUser;
  
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CommentRepository commentRepository;
  
  @Autowired
  private PostRepository postRepository;
  
  @Autowired
  private LikeCRUDRepository likeCRUDRepository;
  
  private List<Userlog> userList;
  
  @PostConstruct
  public void init() {
    userList = new ArrayList<>();
  }
  
  @GetMapping("/post/form")
  public String getPostForm(Model model) {
	  if (this.loggedInUser.getLoggedInUser() == null) {
          return "redirect:/loginpage";
      }
    model.addAttribute("postForm", new AddPostForm());
    userRepository.findAll().forEach(user -> userList.add(user));
    model.addAttribute("userList", userList);
    model.addAttribute("authorid", 0);
    return "forum/postForm";
  }
  
  @PostMapping("/post/add")
  public String addNewPost(@ModelAttribute("postForm") AddPostForm postForm, BindingResult bindingResult, RedirectAttributes attr) throws ServletException {
    if (bindingResult.hasErrors()) {
      System.out.println(bindingResult.getFieldErrors());
      attr.addFlashAttribute("org.springframework.validation.BindingResult.post", bindingResult);
      attr.addFlashAttribute("post", postForm);
      return "redirect:/forum/post/form";
    }
    Optional<Userlog> user = userRepository.findById( (int) loggedInUser.getLoggedInUser().getId());
   
    if (user.isEmpty()) {
      throw new ServletException("Something went seriously wrong and we couldn't find the user in the DB");
    }
    Post post = new Post();
    post.setAuthor(user.get());
    post.setContent(postForm.getContent());
    postRepository.save(post);
    return String.format("redirect:/forum/post/%d", post.getId());
  }  
  @GetMapping("/post/{id}")
  public String postDetail(@PathVariable int id, Model model) throws ResourceNotFoundException {
    Optional<Post> post = postRepository.findById(id);
    if (post.isEmpty()) {
      throw new ResourceNotFoundException("No post with the requested ID");
    }
    model.addAttribute("post", post.get());
    model.addAttribute("userList", userList);
    int numLikes = likeCRUDRepository.countByLikeIdPost(post.get());
    model.addAttribute("likeCount", numLikes);
    return "forum/postDetail";
  }
 
  @GetMapping("/post/{id}/like")
  public String postLike(@PathVariable int id, Integer likerId, RedirectAttributes attr) {
    LikeId likeId = new LikeId();
    Userlog user = this.loggedInUser.getLoggedInUser();
	 likeId.setUser(user);
    likeId.setPost(postRepository.findById(id).get());
    LikeRecord like = new LikeRecord();
    like.setLikeId(likeId);
    likeCRUDRepository.save(like);
    return String.format("redirect:/forum/post/%d", id);
  }
  
  @GetMapping("/post/{postId}/comment/addForm")  
  public String getCommentForm(@PathVariable int postId, Model model) {
      if (loggedInUser.getLoggedInUser() == null) {
          return "redirect:/loginpage";
      }

      Optional<Post> post = postRepository.findById(postId);
      if (post.isEmpty()) {
          return "redirect:/forum";
      }

      model.addAttribute("post", post.get());
      model.addAttribute("comment", new Comment());
      return "forum/postDetail";
  }

  @PostMapping("/post/{postId}/comment/addComment")  
  public String addComment(@PathVariable int postId, @ModelAttribute Comment comment) throws ServletException {
      Userlog user = loggedInUser.getLoggedInUser();
      if (user == null) {
          throw new ServletException("User is not logged in");
      }
      Optional<Post> post = postRepository.findById(postId);
      if (post.isEmpty()) {
          throw new ServletException("Post not found");
      }

      comment.setPost(post.get());
      comment.setUser(user);

      commentRepository.save(comment);

      return "redirect:/forum/post/" + postId;
  }

   
}