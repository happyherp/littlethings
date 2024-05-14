package de.carlos.hibernateserial;

import java.io.Serializable;

import de.carlos.hibernateserial.models.BaseModel;

public class EntityReference implements Serializable{

    private long id;
    private Class<? extends BaseModel> clazz;

    public EntityReference(BaseModel entity) {

	this.id = entity.getId();
	this.clazz = entity.getClass();
    }

    public Object load() {
	return HibernateUtil.getSessionFactory().getCurrentSession().load(this.clazz, this.id);
    }

}
