/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.mybatisplus.generator.config;

import com.belonk.commons.util.string.StringPool;

import java.nio.charset.StandardCharsets;

/**
 * 定义常量
 *
 * @author YangHu, tangguo, hubin
 * @since 2016-08-31
 */
public interface ConstVal {

    String MODULE_NAME = "ModuleName";

    String ENTITY = "Entity";
    String SERVICE = "Service";
    String SERVICE_IMPL = "ServiceImpl";
    String MAPPER = "Mapper";
    String XML = "Xml";
    String CONTROLLER = "Controller";
    String QUERY = "Query";
    String PAGE = "Page";
    String SCRIPT = "Script";

    String ENTITY_PATH = "entity_path";
    String SERVICE_PATH = "service_path";
    String SERVICE_IMPL_PATH = "service_impl_path";
    String MAPPER_PATH = "mapper_path";
    String XML_PATH = "xml_path";
    String CONTROLLER_PATH = "controller_path";
    String QUERY_PATH = "query_path";
    String PAGE_PATH = "page_path";
    String SCRIPT_PATH = "script_path";

    String JAVA_TMPDIR = "java.io.tmpdir";
    String UTF8 = StandardCharsets.UTF_8.name();
    String UNDERLINE = "_";

    String PAGE_SUFFIX = ".jsp";
    String SCRIPT_SUFFIX = ".js";
    String JAVA_SUFFIX = StringPool.DOT_JAVA;
    String XML_SUFFIX = ".xml";

    String TEMPLATE_ENTITY_JAVA = "/templates/entity.java";
    String TEMPLATE_MAPPER = "/templates/mapper.java";
    String TEMPLATE_XML = "/templates/mapper.xml";
    String TEMPLATE_SERVICE = "/templates/service.java";
    String TEMPLATE_SERVICE_IMPL = "/templates/serviceImpl.java";
    String TEMPLATE_CONTROLLER = "/templates/controller.java";
    String TEMPLATE_QUERY = "/templates/query.java";
    String TEMPLATE_PAGE = "/templates/view.jsp";
    String TEMPLATE_SCRIPT = "/templates/view.js";

    String VM_LOAD_PATH_KEY = "file.resource.loader.class";
    String VM_LOAD_PATH_VALUE = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";

    String SUPER_ENTITY_CLASS = "cn.bookingsmart.entity.BaseEntity";
    // String SUPER_CONTROLLER_CLASS = "cn.bookingsmart.web.BaseController";
    String SUPER_CONTROLLER_CLASS = "";
    String SUPER_MAPPER_CLASS = "cn.bookingsmart.dao.BaseMapper";
    String SUPER_SERVICE_CLASS = "cn.bookingsmart.service.BasePageableService";
    String SUPER_MYBATIS_PAGE_SERVICE_IMPL_CLASS = "cn.bookingsmart.service.impl.BaseMybatisPageableServiceImpl";
    String SUPER_MYBATIS_SERVICE_IMPL_CLASS = "cn.bookingsmart.service.impl.BaseMybatisCrudServiceImpl";

    String SUPER_DATATABLE_QUERY_CLASS = "cn.bookingsmart.query.datatable.DataTableQuery";
    String SUPER_BASE_QUERY_CLASS = "cn.bookingsmart.query.BasePageQuery";

}
