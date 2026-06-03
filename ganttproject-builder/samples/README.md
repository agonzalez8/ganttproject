# Sample project files

## FullFeaturesImport.csv (and variants)

CSV samples that exercise **all built-in task and resource columns**, including **Is critical**.

| File | Use when your short date format is |
|------|-------------------------------------|
| `FullFeaturesImport.csv` | US-style **M/d/yy** (e.g. `6/2/26`) |
| `FullFeaturesImport.dd-MM-yyyy.csv` | **dd/MM/yyyy** (e.g. `02/06/2026`, typical UK) |
| `FullFeaturesImport.de.csv` | German **dd.MM.yy** (e.g. `02.06.26`) |

### Check your date format first

1. Open **Settings → UI** (or General).
2. Find the date preview (“Today is …”).
3. Use the matching sample file, **or** edit dates in the CSV so they match that preview **exactly** (including slashes vs dots and 2- vs 4-digit years).

GanttProject rejects dates that do not round-trip to the same string (e.g. `02/06/26` fails on US locale because it is stored as `2/6/26`).

### Column headers must match your UI language

Headers must be the **translated column names** from your GanttProject language (e.g. English: `Name`, `Begin date`, `Is critical`). If the UI is Spanish, export a blank project once and copy header names from that file.

### How to import

1. Pick the CSV variant that matches your **date format** and **language**.
2. **Project → Import** → select the file.
3. Review the import report; warnings about dates or unknown columns usually mean a locale mismatch.

### Column reference (English UI)

**Tasks:** ID, Name (required), Begin date, End date, Web Link, Notes, Completion, Coordinator, Resources (`Alice;Bob`), Assignments (`1:100.00`), Duration, Predecessors (`1` or `2-FS=P2D`), Outline number, Cost, Task color, Priority (`0`–`4`), Is critical (`true`/`false`).

**Resources** (after a blank line): ID, Name (required), e-mail, Phone, Default role, Standard rate, Total cost, Total load.

### Related

- `HouseBuildingSample.gan` — native `.gan` sample
- `docs/ADDING_NEW_TASK_COLUMN.md` — column implementation notes
