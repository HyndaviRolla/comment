package com.example.demo.repository;
import org.springframework.data.repository.CrudRepository;
import com.example.demo.entity.LikeId;
import com.example.demo.entity.LikeRecord;
import com. example.demo.entity.Post;
public interface LikeCRUDRepository extends CrudRepository<LikeRecord, LikeId>{
  public Integer countByLikeIdPost(Post post);
}