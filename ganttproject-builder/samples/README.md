# Sample project files

All CSV examples are available in **three date formats**. Pick the file whose suffix matches your **Settings → date preview** (English column headers).

| Suffix | Date format | Example |
|--------|-------------|---------|
| `.csv` | US **M/d/yy** | `6/2/26` |
| `.dd-MM-yyyy.csv` | **dd/MM/yyyy** | `02/06/2026` |
| `.de.csv` | German **dd.MM.yy** | `02.06.26` |

**Project → Import** → choose the matching file. Headers must match your UI language (these use English).

---

## ProgressVsResourceLoad

Shows that **Completion %** and **resource Unit %** are independent.

| Task | Completion | Resource / Unit | Lesson |
|------|------------|-----------------|--------|
| Half done but still full-time on Alice | **50%** | Alice, full (**100**) | Progress ≠ less load |
| Not started but only half capacity on Bob | **0%** | Bob, **50** | Low Unit = part-time plan |
| Fully done and no resources | **100%** | (none) | Done, no assignments |

| File |
|------|
| `ProgressVsResourceLoad.csv` |
| `ProgressVsResourceLoad.dd-MM-yyyy.csv` |
| `ProgressVsResourceLoad.de.csv` |

After import, check the **Resources** chart: Alice stays fully loaded for the full date range at 50% completion.

The **Resources** column assigns people at **100%** unit load (same as adding them in the UI). Use **Assignments** (`1:50.00`) when you need a different load.

---

## FullFeaturesImport

All built-in task and resource columns, including **Is critical**.

| File |
|------|
| `FullFeaturesImport.csv` |
| `FullFeaturesImport.dd-MM-yyyy.csv` |
| `FullFeaturesImport.de.csv` |

**Tasks:** ID, Name, Begin/End date, Web Link, Notes, Completion, Coordinator, Resources, Assignments, Duration, Predecessors, Outline number, Cost, Task color, Priority, Is critical.

**Resources** (after a blank line): ID, Name, e-mail, Phone, Default role, Standard rate, Total cost, Total load.

---

## Related

- `HouseBuildingSample.gan` — native `.gan` sample
- `docs/ADDING_NEW_TASK_COLUMN.md` — column implementation notes
