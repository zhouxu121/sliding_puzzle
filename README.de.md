# Schiebepuzzle

[中文](README.zh-CN.md) | [English](README.md) | **Deutsch**

Eine Java-Swing-Umsetzung des klassischen Schiebepuzzles. Das Spiel kann mit Zahlen gespielt werden oder ein eigenes Bild in ein Puzzle verwandeln.

## Aktueller Stand

**v1.1.0 — Bildpuzzle-Modus**

Das Projekt bietet einen spielbaren Zahlen- und Bildpuzzle-Modus. Über **Bild hochladen** kann ein JPG-, JPEG- oder PNG-Bild ausgewählt und in bewegliche Puzzleteile geteilt werden. Mit **Zahlen anzeigen** wird zur Zahlendarstellung zurückgewechselt, ohne das laufende Spiel zurückzusetzen.

## Funktionen

- Spielfelder mit 3 × 3, 4 × 4 und 5 × 5 Feldern
- Lösbare, zufällig erzeugte Puzzles durch gültige Züge
- Zahlenpuzzle- und Bildpuzzle-Modus
- Unterstützung für JPG-, JPEG- und PNG-Bilder
- Automatisch auf 600 × 600 skalierte Bildfläche ohne Zwischenräume
- Wechsel zwischen Bild- und Zahlendarstellung ohne Verlust des aktuellen Spielstands
- Prüfung gültiger Züge und Schrittzähler
- Schwierigkeitsauswahl und Neustart des aktuellen Spiels
- Gewinnerkennung mit Abschlussdialog
- Java-Swing-Oberfläche mit getrenntem Spielmodell und GUI

## Voraussetzungen

- Java Development Kit (JDK) 8 oder neuer
- BlueJ (optional)

Java-Installation prüfen:

```bash
java --version
javac --version
```

## Aus dem Quellcode starten

Repository klonen:

```bash
git clone https://github.com/zhouxu121/sliding_puzzle.git
cd sliding_puzzle/SlidingPuzzle
```

Kompilieren und starten:

```bash
javac *.java
java Main
```

## Mit BlueJ starten

1. BlueJ öffnen.
2. **Open Project** auswählen.
3. Den Ordner `SlidingPuzzle` öffnen.
4. Alle Klassen kompilieren.
5. Mit der rechten Maustaste auf `Main` klicken und `void main(String[] args)` ausführen.

## Spielanleitung

1. Im Schwierigkeitsmenü eine Spielfeldgröße auswählen.
2. Ein direkt an das leere Feld angrenzendes Teil anklicken.
3. Das Teil bewegt sich in das leere Feld und der Schrittzähler steigt.
4. Alle Teile von links nach rechts und von oben nach unten anordnen, um das Puzzle zu lösen.

Für ein Bildpuzzle im Menü **Bild hochladen** wählen und eine JPG-, JPEG- oder PNG-Datei auswählen. Die aktuelle Anordnung und Schrittzahl bleiben erhalten. Mit **Zahlen anzeigen** wird wieder die Zahlendarstellung angezeigt.

Gelöstes 3-×-3-Puzzle:

```text
0  1  2
3  4  5
6  7  [ ]
```

Nur Teile unmittelbar oberhalb, unterhalb, links oder rechts des leeren Felds können bewegt werden.

## Projektstruktur

```text
sliding_puzzle/
├── SlidingPuzzle/
│   ├── Main.java          # Programmeinstieg
│   ├── GameJFrame.java    # Swing-Benutzeroberfläche
│   ├── GameModel.java     # Puzzle-Regeln und Spielfeldzustand
│   └── package.bluej      # BlueJ-Projektkonfiguration
├── .gitignore
├── README.md              # Englisch
├── README.zh-CN.md        # Chinesisch
└── README.de.md           # Deutsch
```

## Zentrale Klassen

### `GameModel`

Enthält die Spiellogik. Die Klasse erstellt ein gelöstes Spielfeld, mischt es durch gültige Züge, prüft Züge, speichert den Spielfeldzustand und erkennt ein gelöstes Puzzle.

### `GameJFrame`

Enthält die Swing-Oberfläche. Die Klasse zeigt Zahlen- oder Bildteile an, verarbeitet das Laden von Bildern und den Wechsel der Darstellung, stellt das Spielmenü bereit und zeigt den Abschlussdialog.

### `Main`

Startet die Anwendung im Event-Dispatch-Thread von Swing.

## Lizenz

Erstellt zu Lern- und Unterrichtszwecken.
