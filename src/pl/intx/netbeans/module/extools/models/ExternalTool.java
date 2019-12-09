/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.intx.netbeans.module.extools.models;

import java.io.Serializable;

/**
 *
 * @author mariuszclapinski
 */
public final class ExternalTool implements Serializable{

    private static final String FALLBACK_NAME = "New Tool";
    private static final String FALLBACK_PATH = null;
    
    private String name;
    private String path;
    private String args;

    public ExternalTool() {
        this.setName(ExternalTool.FALLBACK_NAME);
        this.setPath(ExternalTool.FALLBACK_PATH);
    }

    public ExternalTool(String name, String path) {
        this.name = name;
        this.path = path;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }
}
