package com.example.demo.entity;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
@Entity
public class Post {
	@Id
	  @GeneratedValue(strategy=GenerationType.IDENTITY)
	  private int id;
	  
	  private String content;
	  
	  @ManyToOne(cascade = CascadeType.ALL)
	  @JoinColumn(name = "author_id", referencedColumnName = "id")
	  private Userlog author;
  public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Userlog getAuthor() {
		return author;
	}

	public void setAuthor(Userlog author) {
		this.author = author;
	}

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private List<Comment> comments;
 
}
