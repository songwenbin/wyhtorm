# wyhtorm

## 为什么要造轮子?

因项目需要，没有找符合项目需求的ORM框架，所以手写一个符合自身需求的框架。

个人自用，需者可取。

## 使用方式

```Java
插入
Model employee = new Model("TableName")
employee.set("first_name", "wheel")
   .set("last_name", "new");
employee.save();

查询
List<Model> employees = Model.getM("TableName").findAll();
```

# 特性支持
- [*] 动态key，value形式存储数据库
- [] JSON直接存储到数据库

