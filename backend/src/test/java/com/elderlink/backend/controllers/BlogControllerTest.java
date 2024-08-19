package com.elderlink.backend.controllers;
import com.elderlink.backend.domains.dto.BlogDto;
import com.elderlink.backend.domains.entities.BlogEntity;
import com.elderlink.backend.mappers.Mapper;
import com.elderlink.backend.services.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BlogControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private BlogService blogService;

    @Mock
    private Mapper<BlogEntity, BlogDto> blogMapper;

    @InjectMocks
    private BlogController blogController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(blogController).build();
    }

    @Test
    void createBlog_Returns201OnSuccess() throws Exception {
        BlogDto blogDto = new BlogDto(null, "Test Title", "Test Body", 1L);
        BlogEntity blogEntity = new BlogEntity(1L, null, "Test Title", "Test Body");

        when(blogMapper.toEntity(any(BlogDto.class))).thenReturn(blogEntity);
        when(blogService.createBlog(any(BlogEntity.class))).thenReturn(blogEntity);
        when(blogMapper.toDto(any(BlogEntity.class))).thenReturn(blogDto);

        mockMvc.perform(post("/api/blog/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blogDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Title"));

        verify(blogMapper).toEntity(any(BlogDto.class));
        verify(blogService).createBlog(any(BlogEntity.class));
        verify(blogMapper).toDto(any(BlogEntity.class));
    }

    @Test
    void deleteBlog_Success() throws Exception {
        Long blogId = 1L;

        mockMvc.perform(delete("/api/blog/delete/{id}", blogId))
                .andExpect(status().isNoContent());

        verify(blogService).deleteBlog(blogId);
    }

    @Test
    void getAllBlogs_Success() throws Exception {
        List<BlogEntity> blogs = Collections.singletonList(
                new BlogEntity(1L, null, "Blog Title", "Blog Body")
        );

        when(blogService.getBlogs()).thenReturn(blogs);

        mockMvc.perform(get("/api/blog/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Blog Title"));

        verify(blogService).getBlogs();
    }
    @Test
    void updateBlog_Success() throws Exception {
        Long blogId = 1L;
        BlogDto updateBlogDto = new BlogDto(blogId, "Updated Title", "Updated Body", 1L);
        BlogEntity updatedBlogEntity = new BlogEntity(blogId, null, "Updated Title", "Updated Body");

        when(blogMapper.toEntity(any(BlogDto.class))).thenReturn(updatedBlogEntity);
        when(blogService.updateBlog(eq(blogId), any(BlogEntity.class))).thenReturn(updatedBlogEntity);
        when(blogMapper.toDto(any(BlogEntity.class))).thenReturn(updateBlogDto);


        mockMvc.perform(patch("/api/blog/{blogId}", blogId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBlogDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.body").value("Updated Body"));

        verify(blogMapper).toEntity(any(BlogDto.class));
        verify(blogService).updateBlog(eq(blogId), any(BlogEntity.class));
        verify(blogMapper).toDto(any(BlogEntity.class));
    }
}
