package com.dmm.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmm.task.data.entity.Posts;

public interface PostsRepository extends JpaRepository<Posts, Integer> {

}