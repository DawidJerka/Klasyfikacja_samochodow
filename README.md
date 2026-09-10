# 🚗 Car Logo Detection

A machine learning project focused on recognizing car brands from images and video recordings.

The project covers the complete ML workflow — from dataset analysis and experimentation with different modeling approaches, through explainability and energy-efficiency analysis, to deployment of the final model in an Android application.

The final solution uses **YOLOv8n** to detect car brand logos and performs inference directly on the mobile device using **TensorFlow Lite**.

---

## 🎯 Project Goal

The goal of the project was to create a system capable of:

* analyzing images and video recordings,
* detecting car brand logos,
* recognizing the corresponding car brand,
* displaying detected logos with bounding boxes and class labels,
* running inference directly on an Android device.

Initially, the problem was approached as an image classification task using **MobileNet**. However, the results were not satisfactory enough for the intended use case.

This led to a change in approach from whole-image classification to **logo detection** using YOLO.

---

## 🧠 Modeling Approach

### MobileNet Classification

The first version of the system used **MobileNet** to classify the car brand based on the entire image.

During experimentation, this approach did not provide sufficiently reliable results. Car images contain large amounts of visual information, while the brand logo often occupies only a relatively small part of the frame.

Because of this, the task was reformulated as an **object detection problem**.

### YOLOv8n Logo Detection

The final approach uses **YOLOv8n** to detect car manufacturer logos.

Several experiments were performed involving:

* different numbers of classes,
* reduced class sets,
* data augmentation,
* preprocessing strategies,
* model hyperparameters.

The final model was trained on a **reduced set of classes with data augmentation**, which provided the best balance between model quality and practical usability.

---

## 📊 Exploratory Data Analysis

Before training the models, the dataset was analyzed to better understand its structure and potential limitations.

The analysis included:

* number of available classes,
* class distribution and imbalance,
* number of samples per brand,
* image dimensions and resolutions,
* dataset completeness,
* visual diversity of available examples.

The results of the analysis were used to guide later decisions regarding preprocessing, augmentation and class selection.

---

## 🛠️ Data Preprocessing

The preprocessing pipeline included operations such as:

* image resizing,
* normalization,
* scaling,
* preparing annotations for model training,
* adapting the dataset to the requirements of the selected architectures.

Different preprocessing configurations were evaluated to determine their influence on model performance.

---

## 🔄 Data Augmentation

To improve model generalization, data augmentation techniques were introduced during training.

The tested transformations included:

* rotations,
* brightness changes,
* contrast adjustments,
* horizontal transformations,
* other image variations simulating different recording conditions.

Models trained with and without augmentation were compared to evaluate its impact on detection performance.

---

## 🔍 Explainability

To better understand what visual features influenced the model's predictions, an explainability analysis was performed using **EigenCAM**.

The analysis made it possible to visualize regions of the image that contributed most strongly to the model's decisions.

This was particularly useful for:

* inspecting correct predictions,
* analyzing false detections,
* identifying problematic examples,
* understanding whether the model focused on relevant logo features.

---

## 🌱 Green AI Analysis

The project also included an analysis of the computational and environmental cost of model training.

Using **CodeCarbon**, information related to the following aspects was measured:

* training duration,
* computational resource usage,
* estimated energy consumption,
* estimated carbon emissions.

This allowed the models to be considered not only in terms of predictive performance, but also computational efficiency.

---

## 📱 Android Deployment

The trained YOLOv8n model was converted to **TensorFlow Lite (TFLite)** and integrated into an Android application.

The application allows the user to select a video recording and then processes its frames directly on the device.

For every analyzed frame, the application:

1. prepares the frame for model inference,
2. runs the TensorFlow Lite model,
3. processes the detected objects,
4. draws bounding boxes around detected logos,
5. displays the predicted car brand.

The user can also control how frequently frames are analyzed, allowing the application to adapt the computational workload to the capabilities of the device.

---

## 🎥 Video Analysis

The Android application supports video-based inference.

Detected logos are presented directly on the video frame using:

* **bounding boxes**,
* **predicted brand names**.

The frame-processing frequency can be adjusted by the user, allowing a trade-off between detection responsiveness and device performance.

---

## 🔄 Project Workflow

The project covered the following stages:

1. **Project preparation and documentation**

   * repository setup,
   * project planning,
   * documentation of development stages.

2. **Exploratory Data Analysis**

   * class analysis,
   * dataset balance,
   * image quality and resolution analysis,
   * dataset diversity evaluation.

3. **Preprocessing**

   * resizing,
   * normalization,
   * data preparation.

4. **Data augmentation**

   * introduction of image transformations,
   * comparison of models trained with and without augmentation.

5. **Model development**

   * MobileNet-based classification experiments,
   * transition to YOLO-based logo detection,
   * YOLOv8n training and optimization.

6. **Explainability**

   * EigenCAM visualization,
   * analysis of model predictions and errors.

7. **Green AI analysis**

   * training resource monitoring,
   * energy-efficiency analysis using CodeCarbon.

8. **Team organization**

   * Kanban-based task management,
   * tracking implementation progress.

9. **Model deployment**

   * YOLOv8n conversion to TensorFlow Lite,
   * integration with Android.

10. **Practical verification**

    * testing the application on video recordings,
    * visual verification of detected logos and predicted brands.

---

## 🧰 Technologies

* **Python**
* **YOLOv8 / Ultralytics**
* **MobileNet**
* **TensorFlow**
* **TensorFlow Lite**
* **Android**
* **Java**
* **EigenCAM**
* **CodeCarbon**
* **Computer Vision**
* **Object Detection**
* **Machine Learning**

---

## 👥 Team

The project was developed by a three-person team:

| Name             |
| ---------------- |
| Dawid Jerka      |
| Krzysztof Graj   |
| Jakub Kondraciuk |

---

## 📌 Summary

The project demonstrates the full lifecycle of a practical machine learning solution:

**data preparation → model experimentation → evaluation → explainability → efficiency analysis → mobile deployment**

One of the key decisions during development was changing the original approach when the classification model did not produce satisfactory results.

Moving from whole-image classification with MobileNet to logo detection using YOLOv8n resulted in a solution better suited to the original problem.

The final application demonstrates that the trained model can be deployed on a mobile device and used to analyze video recordings directly on-device.
