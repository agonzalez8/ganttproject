/*
 * Copyright 2026 BarD Software s.r.o., Dmitry Barashev.
 *
 * This file is part of GanttProject, an opensource project management tool.
 *
 * GanttProject is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * GanttProject is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GanttProject.  If not, see <http://www.gnu.org/licenses/>.
 */
package biz.ganttproject.impex.csv

import biz.ganttproject.app.DefaultLocalizer
import biz.ganttproject.app.RootLocalizer
import biz.ganttproject.core.model.task.TaskDefaultColumn
import biz.ganttproject.core.time.TimeUnitStack
import com.google.common.base.Supplier
import junit.framework.TestCase
import net.sourceforge.ganttproject.TestSetupHelper
import net.sourceforge.ganttproject.io.CSVOptions
import net.sourceforge.ganttproject.resource.HumanResourceManager
import net.sourceforge.ganttproject.roles.RoleManager
import net.sourceforge.ganttproject.roles.RoleManagerImpl
import net.sourceforge.ganttproject.test.task.TaskTestCase
import org.apache.commons.csv.CSVFormat
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.StringReader

class CriticalPathExportImportTest : TaskTestCase() {
  private var myHumanResourceManager: HumanResourceManager? = null
  private var myRoleManager: RoleManager? = null
  private var myTimeUnitStack: TimeUnitStack? = null

  @Throws(Exception::class)
  override fun setUp() {
    RootLocalizer = object : DefaultLocalizer() {
      override fun formatTextOrNull(key: String, vararg args: Any): String? = key
    }
    TaskDefaultColumn.setLocaleApi { key -> key }
    val builder = TestSetupHelper.newTaskManagerBuilder()
    taskManager = builder.build()
    myHumanResourceManager = builder.resourceManager
    myRoleManager = RoleManagerImpl()
    myTimeUnitStack = builder.timeUnitStack
  }

  @Throws(Exception::class)
  fun testExportCriticalPath() {
    taskManager.algorithmCollection.criticalPathAlgorithm.isEnabled = true
    val t1 = createTask()
    t1.setCritical(true)
    val t2 = createTask()
    t2.setCritical(false)

    val options = CSVOptions()
    // Ensure IS_CRITICAL is enabled (it should be by default now)
    assertTrue(
      "IS_CRITICAL option should be present",
      options.taskOptions.containsKey(TaskDefaultColumn.IS_CRITICAL.stub.getID())
    )
    options.taskOptions.get(TaskDefaultColumn.IS_CRITICAL.stub.getID())!!.setValue(true)

    val exporter = GanttCSVExport(taskManager, myHumanResourceManager, myRoleManager, options)
    val outputStream = ByteArrayOutputStream()
    exporter.createWriter(outputStream, SpreadsheetFormat.CSV).use { writer ->
      exporter.save(writer)
    }
    val csvOutput = outputStream.toString()
    val parser = CSVFormat.DEFAULT.withHeader().parse(StringReader(csvOutput))
    val records = parser.records

    TestCase.assertEquals(2, records.size)

    val r1 = records.get(0)
    TestCase.assertEquals("true", r1.get(TaskDefaultColumn.IS_CRITICAL.getName()))

    val r2 = records.get(1)
    TestCase.assertEquals("false", r2.get(TaskDefaultColumn.IS_CRITICAL.getName()))
  }

  @Throws(Exception::class)
  fun testImportCriticalPath() {
    val criticalCol = TaskRecords.TaskFields.IS_CRITICAL.toString()
    val csvData = ("${TaskRecords.TaskFields.NAME},$criticalCol\n"
      + "Task1,true\n"
      + "Task2,false\n")

    val supplier: Supplier<InputStream?> = object : Supplier<InputStream?> {
      override fun get(): InputStream {
        return ByteArrayInputStream(csvData.toByteArray())
      }
    }
    val loader = GanttCSVOpen(
      supplier,
      SpreadsheetFormat.CSV,
      taskManager,
      myHumanResourceManager,
      myRoleManager,
      myTimeUnitStack
    )
    loader.load()

    val tasks = taskManager.getTasks()
    TestCase.assertEquals(2, tasks.size)

    val task1 = tasks.first { it.name == "Task1" }
    val task2 = tasks.first { it.name == "Task2" }
    TestCase.assertTrue("Task1 should be critical after CSV import", task1.isCritical())
    TestCase.assertFalse("Task2 should not be critical after CSV import", task2.isCritical())
  }
}
