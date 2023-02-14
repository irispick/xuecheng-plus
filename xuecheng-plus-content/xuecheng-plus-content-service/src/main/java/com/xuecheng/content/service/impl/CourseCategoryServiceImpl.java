package com.xuecheng.content.service.impl;

import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Iris
 * @version 1.0
 * @description
 * @date 2023/2/13 20:34
 */
@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {

    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
        // 得到了根结点下边的所有子结点
        List<CourseCategoryTreeDto> categoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);

        //定义 一个List作为最终返回的数据
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = new ArrayList<>();

        // 为了方便找子结点的父结点，定义一个map
        HashMap<String, CourseCategoryTreeDto> nodeMap = new HashMap<>();

        // 将数据封装到List中，只包括根结点的直接下属结点
        categoryTreeDtos.stream().forEach(item -> {
            nodeMap.put(item.getId(), item);

            // 将根结点的直接下属结点添加到List中
            if (item.getParentid().equals(id)) {
                courseCategoryTreeDtos.add(item);
            }
            // 找到该节点的父结点
            String parentid = item.getParentid();
            CourseCategoryTreeDto parentNode = nodeMap.get(parentid);
            // 判断父结点是否存在
            if (parentNode != null) {
                // 如果父结点存在，找到父结点的子结点列表
                List childrenTreeNodes = parentNode.getChildrenTreeNodes();
                if (childrenTreeNodes == null) {
                    // 如果子结点列表为空，创建一个子结点列表
                    parentNode.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
                }
                // 找到子结点，放到它的父结点的childrenTreeNodes属性中
                parentNode.getChildrenTreeNodes().add(item);
            }

        });

        // 返回的list中只包括了根结点的直接下属结点
        return courseCategoryTreeDtos;
    }
}
