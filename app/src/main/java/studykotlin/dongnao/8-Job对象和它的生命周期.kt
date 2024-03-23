package studykotlin.dongnao

/**
 * Job对象
 * 1.通过launch或async函数创建的协程，都会返回一个job实例，
 * 该实例是协程的唯一标识，并负责管理协程的生命周期。
 *2.一个job任务的状态：新创建（New）、活跃（Active）、完成中（Completing）
 * 已完成（Completed）、取消中（Cancelling）、已取消（canceled）。
 * 虽然我们无法访问这些状态，可以通过Job的属性：isActive、isCancelled、isCompleted
 *
 * Job生命周期：
 *  生命周期：新创建（New）、活跃（Active）、完成中（Completing）、已完成（Completed）、取消中（Cancelling）、已取消（canceled）
 *
 *  如果协程处于活跃状态，协程出错或者调用job.cancel()都会将当前任务置为取消中（Cancelling）状态（isActive = false，isCancelling =true）.
 *  当所有子协程都完成后，协程会进入已取消（Canceled）状态，此时isCompleted = true。
 */
















