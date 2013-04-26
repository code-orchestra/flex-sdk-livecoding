package codeOrchestra.digest.impl;

import codeOrchestra.digest.IParameter;

/**
 * @author Alexander Eliseyev
 */
public class SWCParameter implements IParameter {

  private String name;
  private String type;
  
  public SWCParameter(String name, String type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }
  
}