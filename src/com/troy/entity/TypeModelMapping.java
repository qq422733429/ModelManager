package com.troy.entity;



/**
 * TypeModelMapping entity. @author MyEclipse Persistence Tools
 */

public class TypeModelMapping  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private String modelId;
     private String typeId;


    // Constructors

    /** default constructor */
    public TypeModelMapping() {
    }

    
    /** full constructor */
    public TypeModelMapping(String modelId, String typeId) {
        this.modelId = modelId;
        this.typeId = typeId;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getModelId() {
        return this.modelId;
    }
    
    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getTypeId() {
        return this.typeId;
    }
    
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
   








}