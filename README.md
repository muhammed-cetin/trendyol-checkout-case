# Trendyol Checkout Case

E-commerce shopping cart application developed with clean code principles, SOLID design patterns, and Domain-Driven Design (DDD).

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Business Rules](#business-rules)
- [Commands](#commands)
- [Running Tests](#running-tests)
- [Design Patterns](#design-patterns)

---

## ✨ Features

- ✅ Shopping cart management (add, remove, reset)
- ✅ Multiple item types (Default, Digital, VasItem)
- ✅ Automatic promotion calculation (3 promotion types)
- ✅ Business rule validation
- ✅ JSON-based command processing
- ✅ File I/O operations
- ✅ Comprehensive unit tests

---

## 🛠 Tech Stack

- **Java:** 17+
- **Build Tool:** Maven
- **Testing:** JUnit 5
- **JSON Processing:** Gson 2.10.1
- **Design:** Domain-Driven Design (DDD)

---

## 📁 Project Structure
```
src/main/java/com/trendyol/checkout/
├── domain/
│   ├── cart/
│   │   └── Cart.java                    # Aggregate Root
│   ├── item/
│   │   ├── Item.java                    # Abstract base class
│   │   ├── DefaultItem.java             # Standard items
│   │   ├── DigitalItem.java             # Digital items (Steam cards, etc.)
│   │   └── VasItem.java                 # Value-added services
│   ├── promotion/
│   │   ├── Promotion.java               # Strategy interface
│   │   ├── SameSellerPromotion.java     # 10% discount
│   │   ├── CategoryPromotion.java       # 5% discount
│   │   ├── TotalPricePromotion.java     # Tiered discounts
│   │   └── PromotionService.java        # Best promotion selector
│   └── exception/
│       ├── CartException.java
│       └── ItemException.java
├── service/
│   └── CartService.java                 # Application service
├── command/
│   ├── CommandRequest.java              # Input DTO
│   └── CommandResponse.java             # Output DTO
├── io/
│   ├── JsonMapper.java                  # JSON parser
│   ├── InputReader.java                 # File reader
│   └── OutputWriter.java                # File writer
└── Application.java                     # Main entry point

src/test/java/com/trendyol/checkout/
└── domain/
    └── cart/
        └── CartTest.java                # Comprehensive unit tests
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Installation

1. **Clone the repository**
```bash
   git clone <repository-url>
   cd trendyol-checkout-case
```

2. **Build the project**
```bash
   mvn clean install
```

3. **Run tests**
```bash
   mvn test
```

---

## 💻 Usage

### Running the Application

1. **Create `input.txt`** in the project root:
```json
   {"command":"addItem","payload":{"itemId":1,"categoryId":1001,"sellerId":500,"price":1000.0,"quantity":2}}
   {"command":"displayCart"}
   {"command":"resetCart"}
```

2. **Run the application:**
```bash
   # Using Maven
   mvn exec:java -Dexec.mainClass="com.trendyol.checkout.Application"
   
   # Or using IntelliJ
   Right-click Application.java → Run 'Application.main()'
```

3. **Check `output.txt`** for results:
```json
   {"result":true,"message":"Item added to cart successfully"}
   {"result":true,"message":{...}}
   {"result":true,"message":"Cart reset successfully"}
```

---

## 📊 Business Rules

### Cart Limits
- **Max unique items:** 10 (excluding VasItems)
- **Max total items:** 30 (including VasItems)
- **Max total amount:** 500,000 TL

### Item Types

#### DefaultItem
- Standard e-commerce products
- Max quantity per item: 10
- Can have VasItems (only Furniture & Electronics)

#### DigitalItem
- Steam cards, donation cards, etc.
- CategoryID: **7889**
- Max quantity: 5

#### VasItem
- Value-added services (insurance, assembly)
- CategoryID: **3242**, SellerID: **5003**
- Only for Furniture (1001) & Electronics (3004)
- Max 3 per DefaultItem
- Price cannot exceed DefaultItem price

### Promotions

| Promotion | ID | Condition | Discount |
|-----------|----|----|----------|
| SameSellerPromotion | 9909 | All items same seller | 10% |
| CategoryPromotion | 5676 | CategoryID 3003 | 5% |
| TotalPricePromotion | 1232 | Price tiers | 250-2000 TL |

**Note:** Only the best promotion (maximum discount) is applied.

---

## 🎮 Commands

### addItem
**Input:**
```json
{"command":"addItem","payload":{"itemId":123,"categoryId":1001,"sellerId":500,"price":1000.0,"quantity":2}}
```
**Output:**
```json
{"result":true,"message":"Item added to cart successfully"}
```

### addVasItemToItem
**Input:**
```json
{"command":"addVasItemToItem","payload":{"itemId":123,"vasItemId":10,"vasCategoryId":3242,"vasSellerId":5003,"price":200.0,"quantity":1}}
```

### removeItem
**Input:**
```json
{"command":"removeItem","payload":{"itemId":123}}
```

### resetCart
**Input:**
```json
{"command":"resetCart"}
```

### displayCart
**Input:**
```json
{"command":"displayCart"}
```
**Output:**
```json
{
  "result": true,
  "message": {
    "items": [...],
    "totalAmount": 6750.0,
    "appliedPromotionId": 9909,
    "totalDiscount": 750.0
  }
}
```

---

## 🧪 Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=CartTest

# Run with coverage
mvn test jacoco:report
```

### Test Coverage
- ✅ Cart operations (add, remove, reset)
- ✅ Boundary value testing (limits)
- ✅ Exception handling
- ✅ VasItem validations
- ✅ Price calculations
- ✅ Promotion logic

---

## 🎨 Design Patterns

### Strategy Pattern
Used for promotion calculation:
```java
public interface Promotion {
    double calculateDiscount(Cart cart);
    boolean isApplicable(Cart cart);
}
```

### Factory Pattern
Used for item creation:
```java
private Item createItem(int categoryId, ...) {
    if (categoryId == DIGITAL_CATEGORY_ID) {
        return new DigitalItem(...);
    }
    return new DefaultItem(...);
}
```

### Domain-Driven Design (DDD)
- **Aggregate Root:** Cart
- **Entities:** Item, DefaultItem, DigitalItem
- **Value Objects:** VasItem
- **Domain Services:** PromotionService

---

## 🏗️ SOLID Principles

- ✅ **Single Responsibility:** Each class has one reason to change
- ✅ **Open/Closed:** Open for extension, closed for modification
- ✅ **Liskov Substitution:** Subtypes are substitutable for base types
- ✅ **Interface Segregation:** Client-specific interfaces
- ✅ **Dependency Inversion:** Depend on abstractions, not concretions

---

## 📝 Example Scenarios

### Scenario 1: Multiple Items with Promotion
```json
{"command":"addItem","payload":{"itemId":1,"categoryId":1001,"sellerId":500,"price":1000.0,"quantity":2}}
{"command":"addItem","payload":{"itemId":2,"categoryId":3004,"sellerId":500,"price":5000.0,"quantity":1}}
{"command":"displayCart"}
```

**Result:**
- Total Price: 7,000 TL
- Promotion: SameSellerPromotion (10%)
- Discount: 700 TL
- Final Amount: 6,300 TL

### Scenario 2: Item with VasItem
```json
{"command":"addItem","payload":{"itemId":1,"categoryId":3004,"sellerId":500,"price":5000.0,"quantity":1}}
{"command":"addVasItemToItem","payload":{"itemId":1,"vasItemId":10,"vasCategoryId":3242,"vasSellerId":5003,"price":500.0,"quantity":1}}
{"command":"displayCart"}
```

**Result:**
- Item: 5,000 TL
- VasItem: 500 TL
- Total: 5,500 TL
- Promotion: SameSellerPromotion (10%)
- Final: 4,950 TL

---

