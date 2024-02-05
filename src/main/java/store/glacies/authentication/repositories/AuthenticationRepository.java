package store.glacies.authentication.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import store.glacies.authentication.models.AuthenticationInfo;

@Repository
public interface AuthenticationRepository extends CrudRepository<AuthenticationInfo,String> {
}
