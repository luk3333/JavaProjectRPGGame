# JavaProjectRPGGame
Projekt w JAVA na przedmiot Programowanie Obiektowe, o tematyce: Gra RPG – bohaterowie, ekwipunek, potwory, walka turowa, zapis gry.
Funkcjonalności:
1. System bohatera

Tworzenie nowej postaci (wybór imienia, klasy/typu bohatera) posiadającej statystyki: zdrowie (HP), atak, obrona, poziom, doświadczenie.

Zdobywanie doświadczenia i awansowania na wyższe poziomy.

Wyświetlanie informacji o bohaterze.

2. Ekwipunek (inventory)

Przechowywanie przedmiotów (np. broń, zbroja, mikstury).

Podnoszenie przedmiotów.

Wyświetlanie ekwipunku.

Wyposażanie/zmiana aktualnie używanej broni lub zbroi.

Używanie przedmiotów (np. mikstura lecząca).

3. Potwory

Generowanie przeciwników o różnych statystykach.

Wyświetlanie informacji o napotkanym potworze.

4. Walka turowa

Rozpoczynanie walki po napotkaniu potwora, w której gracz atakuje, używa przedmiotu, a potwór zawsze atakuje.

Obliczanie obrażeń z uwzględnieniem statystyk.

Zakończenie walki w przypadku spadku zdrowia (HP) bohatera lub przeciwnika.

Wylosowanie nagrody za wygraną walkę.

5. Zapis i wczytywanie gry

Zapis stanu gry do pliku (dane bohatera, ekwipunek, aktualny poziom).

Wczytanie gry przy uruchomieniu programu.

# Przewodnik konfiguracji i uruchamiania

## Wymagania wstępne
- **Java Development Kit (JDK)** w wersji 11 lub wyższej
- **Git** (do klonowania repozytorium)

## Instalacja

### 1. Klonowanie repozytorium
```bash
git clone https://github.com/luk3333/JavaProjectRPGGame.git
cd JavaProjectRPGGame
```

### 2. Kompilacja projektu
```bash
javac -d bin src/main/java/rpg/*.java
```

### 3. Uruchomienie gry
```bash
java -cp bin rpg.Main
```

## Jak grać

### Menu główne
1. **Nowa gra** - Utwórz nową postać wybierając klasę (Wojownik, Łucznik, Mag) i wpisując swoją nazwę
2. **Kontynuuj** - Wczytaj poprzednio zapisaną grę

### Akcje w grze
- **Statystyki** - Wyświetl zdrowie, atak, obronę, poziom i doświadczenie postaci
- **Ekwipunek** - Zarządzaj przedmiotami, wyposażaj broń/zbroję i używaj mikstur
- **Eksploruj** - Spotykaj wrogów, znajdź łupy i walcz
- **Zapisz i wyjdź** - Zapisz postęp przed wyjściem z gry

### Walka
- **Atak** - Zadaj obrażenia wrogowi
- **Użyj miksturę** - Spożyj miksturę, aby przywrócić zdrowie

## Struktura projektu
```
src/
├── main/java/rpg/
│   ├── Main.java              - Punkt wejścia
│   ├── Game.java              - Kontroler gry
│   ├── Player.java            - Postać gracza
│   ├── Enemy.java             - Klasa wroga
│   ├── Item.java              - Klasa bazowa przedmiotu
│   ├── Weapon.java            - Przedmioty broni
│   ├── Armor.java             - Przedmioty zbroi
│   ├── Potion.java            - Przedmioty mikstur
│   ├── HeroClass.java         - Enum klasy postaci
│   ├── EnemyType.java         - Enum typu wroga
│   ├── SaveGameManager.java   - Menedżer zapisu/wczytywania
│   ├── InputReader.java       - Interfejs wejścia
│   └── ConsoleInputReader.java- Implementacja wejścia konsolowego
└── test/java/rpg/             - Testy jednostkowe
```

## Zapisywanie gry
Twoja gra jest zapisywana do pliku `savegame.json` w katalogu głównym projektu. Ten plik jest wyłączony z kontroli wersji (zobacz `.gitignore`).


Miłej przygody! 🎮
