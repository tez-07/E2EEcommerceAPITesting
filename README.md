# API Automation Flow (README)

This project demonstrates an end-to-end API automation process involving multiple endpoints — **Login**, **Create Product**, **Create Order**, and **Delete Order** — using **Serialization**, **Deserialization**, and **RequestSpecBuilder** to maintain clean and reusable code.

---

### **1. Login**
**Request Type:** JSON  
**Payload:**  
- `username`  
- `password`

**Process:**
- The request JSON is passed through a **POJO** created for `username` and `password`.
- **Serialization** is used to convert the POJO into JSON for the request body.

**Response:**  
- `Authorization`  
- `userID`  
- `message`

**Deserialization:**  
- The JSON response is stored in a **Response POJO** for further use (e.g., extracting token and userID).

---

### **2. Create Product**
**Request Type:** Form Data  
**Payload:**
- Contains image upload, so `formData` is used.

**Response:**  
- `productId`  
- `message`

**Process:**
- The response is short and simple, so the entire response is stored as a **String** rather than deserializing into a POJO.

---

### **3. Create Order**
**Request Type:** JSON  
**Payload:**
- Uses **Serialization** again.  
- The JSON includes an **Array** of order details:
  - `country`
  - `productOrderedId` (taken from the previous Create Product response)

**Process:**
- The critical part is **Serialization**.
- <img width="1087" height="213" alt="image" src="https://github.com/user-attachments/assets/526bdf68-29ba-47d1-8a68-47670fd41092" />
- <img width="1167" height="520" alt="image" src="https://github.com/user-attachments/assets/7fb299ad-64a7-4f16-a269-2764eab9eecc" />
- Call the `set` methods from the **OrderDetails POJO class**, store them in a list, and then pass this list into the main **Order array**.

**Response:**  
- `orderId`  
- `productOrderedId`  
- `message`

---

### **4. Delete Order**
**Request Type:** JSON  
**Path Parameter:**  
- `productOrderedId` (obtained from the Create Order response)

---

### **5. RequestSpecBuilder**
To reduce duplication across requests, a **Request Specification Builder** is used.  
It defines common components like:
- Base URI  
- Base Path  
- Headers  
- Authorization token  

This avoids rewriting the same configurations for each request.

---

### **6. Authorization Handling**
After login, the **Authorization Token** must be provided for every subsequent request.  
Unlike browsers, APIs do not automatically handle sessions — the token must be manually included in headers for authentication.

---

### **Summary**
| Endpoint       | Method | Payload Type | Key Technique    | Response Handling      |
|----------------|--------|---------------|------------------|------------------------|
| Login          | POST   | JSON          | Serialization + Deserialization | POJO (User Data) |
| Create Product | POST   | Form Data     | -                | String Response |
| Create Order   | POST   | JSON          | Serialization (Array Handling) | POJO/List |
| Delete Order   | DELETE | Path Param    | -                | - |
| Common Config  | -      | -             | RequestSpecBuilder | Reduces redundancy |

---

**Note:**  
Always include the **Authorization token** obtained from the login response in the header for subsequent API requests to ensure authentication success.
