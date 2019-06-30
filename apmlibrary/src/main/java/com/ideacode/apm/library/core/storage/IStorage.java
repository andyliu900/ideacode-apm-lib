package com.ideacode.apm.library.core.storage;

import com.ideacode.apm.library.core.IInfo;

import java.util.List;

/**
 * APM程序性能监控组件
 *
 * @ProjectName: ideacode_apm
 * @Package: com.ideacode.apm.library.core.storage
 * @ClassName: IStorage
 * @Description:    数据增删改查操作
 * @Author: randysu
 * @CreateDate: 2019-05-21 15:05
 * @UpdateUser:
 * @UpdateDate: 2019-05-21 15:05
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface IStorage {

    String getName();

    IInfo getInfo(Integer id);

    boolean save(IInfo info);

    boolean deleteById(Integer id);

    boolean updateById(Integer id, IInfo info);

    List<IInfo> getAll();

    List<IInfo> getRangeDatas(int index, int count);

    boolean clean();

    boolean cleanByCount(int count);

}
