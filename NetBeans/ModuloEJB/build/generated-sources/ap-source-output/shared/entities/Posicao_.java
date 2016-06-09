package shared.entities;

import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Posicao.class)
public abstract class Posicao_ {

	public static volatile SingularAttribute<Posicao, Timestamp> timestamp;
	public static volatile SingularAttribute<Posicao, Integer> id;
	public static volatile SingularAttribute<Posicao, String> lon;
	public static volatile SingularAttribute<Posicao, String> login;
	public static volatile SingularAttribute<Posicao, String> lat;

}

