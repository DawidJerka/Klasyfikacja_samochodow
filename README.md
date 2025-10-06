# 🚗 WTUM – Klasyfikacja samochodów

Projekt realizowany w ramach przedmiotu **Wdrażanie Technik Uczenia Maszynowego (WTUM)**.  
Celem projektu jest stworzenie aplikacji mobilnej wykorzystującej model AI do **rozpoznawania marki samochodu na nagraniu wideo**.

---

## 🎯 Cel projektu

Aplikacja ma umożliwiać:
- wykrycie samochodu na obrazie lub nagraniu,
- rozpoznanie jego marki na podstawie modelu wytrenowanego na publicznie dostępnych zbiorach danych.

Projekt obejmuje:

1. **Przygotowanie projektu i dokumentacji**  
   - utworzenie repozytorium, planu prac oraz dokumentacji opisującej kolejne etapy realizacji.

2. **Eksploracyjna analiza danych**  
   - analiza liczby klas, balansu danych, jakości i rozdzielczości obrazów,  
   - wnioski dotyczące kompletności i różnorodności zbioru.

3. **Przetwarzanie wstępne (preprocessing)**  
   - skalowanie, normalizacja, zmiana rozmiaru obrazów,  
   - ocena wpływu preprocessing’u na wyniki modelu.

4. **Augmentacja danych**  
   - wprowadzenie transformacji (obrót, jasność, kontrast, odbicia),  
   - porównanie skuteczności modeli trenowanych z i bez augmentacji.

5. **Budowa i trening modeli klasyfikacyjnych**  
   - implementacja modeli,  
   - analiza dokładności i strat dla różnych architektur.

6. **Wyjaśnialność modeli (Explainability)**  
   - wizualizacja aktywacji,  
   - interpretacja błędnych predykcji.

7. **Analiza efektywności (Green AI)**  
   - pomiar czasu trenowania i wykorzystania zasobów,  
   - raportowanie metryk efektywności energetycznej.

8. **Organizacja pracy zespołowej**  
   - prowadzenie tablicy zadań w stylu **Kanban**,  
   - śledzenie postępów w realizacji poszczególnych etapów.

9. **Wdrożenie modelu**  
   - przygotowanie wersji do uruchomienia na **smartfonie (Android)**.  

10. **Weryfikacja działania modelu**  
    - test praktyczny w formie **nagrania wideo (film)** prezentującego działanie aplikacji.


---

## 📊 Wykorzystywane zbiory danych (rozważane)

| Nazwa datasetu | Opis | Link |
|-----------------|------|------|
| **UK Car Brands Dataset** | Zdjęcia całych samochodów w podziale na marki. | [Kaggle – UK Car Brands Dataset](https://www.kaggle.com/datasets/bignosethethird/uk-car-brands-dataset) |
| **Vehicle Logos Dataset** | Zdjęcia samych logotypów samochodów z maskami (segmentacja). | [GitHub – vehicle-logos-dataset](https://github.com/GeneralBlockchain/vehicle-logos-dataset) |
| **Car Brand Detection (Roboflow)** | Zdjęcia samochodów z zaznaczonymi bounding boxami (detekcja). | [Roboflow – car_brand_detection](https://universe.roboflow.com/fatince/car_brand_detection-gbnlb) |

Na pierwszym etapie planujemy wykorzystać **UK Car Brands Dataset** do stworzenia prostego modelu klasyfikacyjnego, aby ocenić skuteczność bazowego rozwiązania.

---

## 👥 Zespół projektowy

| Imię i nazwisko |
|-----------------|
| Dawid Jerka | 
| Krzysztof Graj |
| Jakub Kondraciuk |
