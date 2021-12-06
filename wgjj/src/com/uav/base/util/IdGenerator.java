package com.uav.base.util;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;

public class IdGenerator implements Configurable,IdentifierGenerator{
	private String seq_Name="";
	private int seq_len=10;
	
	@Override
	public Serializable generate(SessionImplementor arg0, Object arg1) throws HibernateException {
		return SeqUtil.getNextSeq(this.seq_Name, this.seq_len);
	}


	@Override
	public void configure(Type type, Properties params, Dialect d) throws MappingException {
		 this.seq_Name=params.getProperty("tableName");
	     this.seq_len =Integer.parseInt(params.getProperty("idLength")); 
	}
}
