# Artifact Catalog

## Overview

Artifact Catalog is a JavaFX desktop application for managing historical artifacts. It allows users to add, remove, and search artifacts based on different criteria and export/import data in JSON format. The application is packaged with a custom runtime and is installable via a Windows installer.

---

## 📦 Installation

### ✅ Option 1: Run from Source (For Developers)

To run and build the project on your local machine:

1. Clone or download the source code from the GitHub repository:
2. Open the project in your preferred Java IDE (e.g., IntelliJ IDEA).
3. Make sure you have Maven installed.
4. Run the following command in the terminal:


"mvn clean package jlink:jlink"
This command will create a custom runtime and package the JAR with its dependencies inside the `/target` directory.

---

### ✅ Option 2: Download and Install (For End Users)

An installer for Windows has been created using **Inno Setup** and is available in the GitHub repository under the [Releases](https://github.com/CemBC/Artifact_Catalog/releases) section.

> ⚠️ **IMPORTANT**  
> If you install the application inside the `Program Files` or `Program Files (x86)` directories, you must run the application as **Administrator** due to write permission restrictions on files like `config.json` and `artifacts.json`.

---

## 📘 USER MANUAL

### ----------------ARTIFACT CATALOG USER GUIDE----------------

1. **Welcome Screen**:
- When you start the application, you will see a welcome screen.
- Click the **"CONTINUE"** button to proceed to the main screen.
- Click the **"HELP"** button to view this user guide.

2. **Main Screen**:
- View a list of existing artifacts.
- To filter artifacts:
  - Enter keywords in the search bar (comma-separated).
  - Select relevant tags (optional).
  - Click the **"Load Artifacts"** button.
- Double-click an artifact to view its details.

3. **Adding an Artifact**:
- Click the **"Add Artifact"** button.
- Fill in required fields: Name, Category, Civilization, Discovery Location, Composition, Date, Dimensions, Weight, Tags, and Image.
- The image size must not exceed **1280x720 pixels**.
- Click **"Save"** to store the artifact.

4. **Removing an Artifact**:
- Select an artifact and click **"Remove Artifact"** to delete it.

5. **Importing/Exporting Data**:
- Use the **File Menu**:
  - **Import Data**: Load JSON file.
    > ⚠️ The imported file will be deleted after use. Tags will be reset.
  - **Export Data**: Save current data to the **Downloads** folder.

6. **Viewing and Editing Details**:
- Double-click an artifact to open its detailed view.
- Use the **Edit** button to modify the artifact.

7. **Image Viewing**:
- Click the **"Image"** button on the detail screen.
- Use mouse wheel to zoom in/out.
- Middle-click to reset image to original size.

8. **Warnings and Errors**:
- Invalid or missing fields are highlighted in red.
- Corrupted or invalid JSON files trigger error messages.

---

## 🔧 Technologies Used

- Java 17
- JavaFX
- Maven
- jlink (for custom runtime)
- Inno Setup (Installer creation)
- Launch4J

---

## 📁 File Structure (Installed Version)

Artifact_Catalog/

│

├─ Artifact_Catalog.exe  
├─ Artifact_Catalog.jar  
├─ artifacts.json  
└─ /my-runtime/ (custom runtime)



---

## 📩 Contact

For bug reports or assistance, contact the development team via GitHub Issues.

---

## 📜 License

This project is made for academic and educational purposes.

