# Common Jmeter Http Sampler Release Note
* 【2022-10-12】原型发布，详见：https://blog.csdn.net/camelials/article/details/127135630
* 【2022-11-23】使用优化：sampleIndex不再对外暴露。之前把sample定义为内置变量，很多函数需要使用，例如：getCsvData(#{csvFileName}, #{varName}, #{sampleIndex})，这样有2个弊端：1：让人困惑：自己根本就没有定义过这样的变量，它是哪里来的？2：函数使用语法不简洁。优化后为：getCsvData(#{csvFileName}, #{varName})，这样就很明确，从哪个csv文件（csvFileName参数表达）去按照顺序读（函数名表达）哪个字段（varName参数表达）。
* 【2023-1-31】采样变量定义及援引有序性支持（之前变量定义为无序方式，造成使用上存在相关约束和限制），该能力支持后，压测测试计划编排将更加符合自然思维，例如：now: getTs();end: mathAdd(#{now}, 3600000);

# TODO
该版本为短时间闭门造车出来的初版，存在以下局限性，后续计划新开分支(分支名待定)替换脚本处理部分。
* 【Done】采样变量申明无序处理带来的问题  
由于对于采样变量的处理是用的无序Map处理（后续有空改为有序Map解决该问题），因此下面的用法不支持（会出错）：
```
--采样变量
now: getTs()
end: mathAdd(#{now}, 3700000)

--Header
192.168.100.62:9030/calendar/v1/event/#{now}/#{end}
```

* 【Done】为了使用的更加直观，#{sampleIndex}参数不再对外暴露。例如：getCsvData(#{csvFileName}, #{varName}, #{sampleIndex}) -> getCsvData(#{csvFileName}, #{varName})

* 脚本处理极度简化带来的问题  -- 后续看时间新开分支实现（因为需要较大的整体变更）  

由于工具开发时间较短（设计，开发时间一共不到10天），脚本处理部分极度简化（300行代码左右）。后续有时间再考虑替换为比较健壮的脚本引擎方式（考虑：分词，语法，四元组，执行器方式的脚本处理引擎），可以参考：遵循编译原理主要过程实现“打印1+1结果”：https://blog.csdn.net/camelials/article/details/123415475
下面的用法目前不支持（函数及参数嵌套）：
```
--采样变量
end: mathAdd(getTs(), 3700000)

--Header
192.168.100.62:9030/calendar/v1/event/getTs()/#{end}
```
