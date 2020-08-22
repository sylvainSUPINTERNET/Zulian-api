package org.zulian.services;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class FolderService {

    FolderService(){}

    public Map<String, String> testFolderService () {
        Map<String, String> map = new HashMap<>();
        map.put("ZG api", "test");
        return map;
    }
}
