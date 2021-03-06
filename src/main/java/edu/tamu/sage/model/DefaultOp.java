package edu.tamu.sage.model;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Default")
public class DefaultOp extends BasicValueOp {

    public final static String TYPE = "DEFAULT_OP";

    public DefaultOp() {
        super();
        setType(TYPE);
    }

    public DefaultOp(String field, String value) {
        this();
        setField(field);
        setValue(value);
    }

    public DefaultOp(String name, String field, String value) {
        this(field, value);
        setName(name);
    }

    @Override
    public void process(Reader reader, Map<String, Collection<Object>> sageDoc) {
        if (!sageDoc.containsKey(getField())) {
            sageDoc.put(getField(), sageDoc.get(getField()).stream().map(value -> getValue()).collect(Collectors.toList()));
        }
    }

    @Override
    public String getType() {
        return TYPE;
    }

}
