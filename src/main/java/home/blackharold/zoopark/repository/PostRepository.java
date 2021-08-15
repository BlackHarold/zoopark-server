package home.blackharold.zoopark.repository;

import home.blackharold.zoopark.entity.Post;
import home.blackharold.zoopark.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserOrderByCreatedDateDesc(User user);

    List<Post> findAllByOrderByCreatedDateDesc();

    Optional<Post> findAllByIdAndUser(Long id, User user);

    Optional<Post> findPostByIdAndUser(Long id, User user);
}
