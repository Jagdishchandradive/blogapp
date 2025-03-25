package com.javajags.blogapp.controller;

import com.javajags.blogapp.config.AppConstants;
import com.javajags.blogapp.payload.ApiResponse;
import com.javajags.blogapp.payload.PostDto;
import com.javajags.blogapp.payload.PostResponse;
import com.javajags.blogapp.service.FileService;
import com.javajags.blogapp.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.LifecycleState;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PostController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    /**
     * Creates a new post for a given user and category.
     *
     * @param postDto    The post data transfer object containing post details.
     * @param userId     The ID of the user creating the post.
     * @param categoryId The ID of the category the post belongs to.
     * @return ResponseEntity containing the created post.
     */
    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto,
                                              @PathVariable Integer userId,
                                              @PathVariable Integer categoryId) {
        logger.info("Creating a new post for User ID: {} in Category ID: {}", userId, categoryId);
        PostDto createPost = this.postService.createPost(postDto, userId, categoryId);
        return new ResponseEntity<PostDto>(createPost, HttpStatus.CREATED);
    }

    //getPostByUser
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity< List<PostDto>> getPostsByUser(@PathVariable Integer userId){
        List<PostDto> postsByUser = this.postService.getPostsByUser(userId);
        //ctrl+alt+v(for variable)
        return new ResponseEntity<List<PostDto>>(postsByUser,HttpStatus.OK);
    }
    // get by category
    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<List<PostDto>>getPostsByCategory(@PathVariable Integer categoryId){
        List<PostDto> postsByCategory = this.postService.getPostsByCategory(categoryId);
        return new ResponseEntity<List<PostDto>>(postsByCategory,HttpStatus.OK);

    }

    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPost(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {

        PostResponse postResponse = this.postService.getAllPost(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
    }


    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {

        PostDto postDto = this.postService.getPostById(postId);
        return new ResponseEntity<PostDto>(postDto, HttpStatus.OK);

    }

    // delete post
    @DeleteMapping("/posts/{postId}")
    public ApiResponse deletePost(@PathVariable Integer postId) {
        this.postService.deletePost(postId);
        return new ApiResponse("Post is successfully deleted !!", true);
    }

    // update post
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) {

        PostDto updatePost = this.postService.updatePost(postDto, postId);
        return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);

    }

    // search
    @GetMapping("/posts/search/{keywords}")
    public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keywords") String keywords) {
        List<PostDto> result = this.postService.searchPosts(keywords);
        return new ResponseEntity<List<PostDto>>(result, HttpStatus.OK);
    }

    // post image upload
    @PostMapping("/post/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image,
                                                   @PathVariable Integer postId) throws IOException {

        PostDto postDto = this.postService.getPostById(postId);
        String fileName = this.fileService.uploadImage(path, image);
        postDto.setImageName(fileName);
        PostDto updatePost = this.postService.updatePost(postDto, postId);
        return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);

    }

    //method to serve files
    @GetMapping(value = "/post/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response
    ) throws IOException {

        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream())   ;

    }




}
