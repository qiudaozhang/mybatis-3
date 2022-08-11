/*
 *    Copyright 2009-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.type;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Clinton Begin
 */
public class BlobByteObjectArrayTypeHandler extends BaseTypeHandler<Byte[]> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Byte[] parameter, JdbcType jdbcType)
    throws SQLException {
    // 输入的参数为一个字节数组， 需要通过 字节输入的输入流类型转换为改类型的对象
    // blob类型需要指定为二进制流的类型进行设置

    ByteArrayInputStream bis = new ByteArrayInputStream(ByteArrayUtils.convertToPrimitiveArray(parameter));
    // 注意最后一个参数是原始的 字节数组的长度
    ps.setBinaryStream(i, bis, parameter.length);
  }

  @Override
  public Byte[] getNullableResult(ResultSet rs, String columnName)
    throws SQLException {
    // 设置的时候没有setBlog 获取的方法有哦
    Blob blob = rs.getBlob(columnName); // 这个方式是通过列名获取
    // 得到Blog后再转换为字节数组
    return getBytes(blob);
  }

  // 下面的两个方法是通过列的所以呢获取
  @Override
  public Byte[] getNullableResult(ResultSet rs, int columnIndex)
    throws SQLException {
    Blob blob = rs.getBlob(columnIndex);
    return getBytes(blob);
  }

  @Override
  public Byte[] getNullableResult(CallableStatement cs, int columnIndex)
    throws SQLException {
    // 这个点区别是 CallableStatement ，前面的是常见的ResultSet里面获取
    Blob blob = cs.getBlob(columnIndex);
    return getBytes(blob);
  }

  private Byte[] getBytes(Blob blob) throws SQLException {
    Byte[] returnValue = null;
    if (blob != null) {
      // 工具类转化，可以学习下怎么转的
      returnValue = ByteArrayUtils.convertToObjectArray(blob.getBytes(1, (int) blob.length()));
    }
    return returnValue;
  }
}
