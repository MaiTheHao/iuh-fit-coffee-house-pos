SET NAMES utf8mb4;

SET CHARACTER SET utf8mb4;

SET
  COLLATION_CONNECTION = utf8mb4_unicode_ci;

SET
  FOREIGN_KEY_CHECKS = 0;

CREATE TABLE
  roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT PK_roles PRIMARY KEY (id),
    CONSTRAINT UQ_roles_code UNIQUE (code),
    INDEX IDX_roles_code (code),
    INDEX IDX_roles_is_active (is_active)
  ) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE
  sizes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT PK_sizes PRIMARY KEY (id),
    CONSTRAINT UQ_sizes_code UNIQUE (code),
    INDEX IDX_sizes_code (code),
    INDEX IDX_sizes_is_active (is_active)
  ) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE
  categories (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    active BOOLEAN DEFAULT TRUE,
    CONSTRAINT PK_categories PRIMARY KEY (id),
    CONSTRAINT UQ_categories_code UNIQUE (code),
    INDEX IDX_categories_code (code),
    INDEX IDX_categories_is_active (is_active)
  ) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE
  employees (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    hashed_password VARCHAR(255) NOT NULL,
    avatar VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    role_id BIGINT NOT NULL,
    CONSTRAINT PK_employees PRIMARY KEY (id),
    CONSTRAINT FK_employees_roles FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT UQ_employees_code UNIQUE (code),
    INDEX IDX_employees_code (code),
    INDEX IDX_employees_role_id (role_id),
    INDEX IDX_employees_phone (phone),
    INDEX IDX_employees_is_active (is_active)
  ) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE
  products (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image VARCHAR(500),
    is_in_stock BOOLEAN DEFAULT TRUE,
    is_active BOOLEAN DEFAULT TRUE,
    category_id BIGINT NOT NULL,
    CONSTRAINT PK_products PRIMARY KEY (id),
    CONSTRAINT FK_products_categories FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT UQ_products_code UNIQUE (code),
    INDEX IDX_products_code (code),
    INDEX IDX_products_category_id (category_id),
    INDEX IDX_products_is_in_stock (is_in_stock),
    INDEX IDX_products_is_active (is_active)
  ) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE
  product_variants (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    price DECIMAL(10, 2) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    product_id BIGINT NOT NULL,
    size_id BIGINT NOT NULL,
    CONSTRAINT PK_product_variants PRIMARY KEY (id),
    CONSTRAINT FK_product_variants_products FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_product_variants_sizes FOREIGN KEY (size_id) REFERENCES sizes (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT UQ_product_variants_code UNIQUE (code),
    CONSTRAINT UQ_product_variants_product_size UNIQUE (product_id, size_id),
    INDEX IDX_product_variants_code (code),
    INDEX IDX_product_variants_product_id (product_id),
    INDEX IDX_product_variants_size_id (size_id),
    INDEX IDX_product_variants_is_active (is_active)
  ) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE
  customers (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    points INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    synced BOOLEAN DEFAULT FALSE,
    CONSTRAINT PK_customers PRIMARY KEY (id),
    CONSTRAINT UQ_customers_code UNIQUE (code),
    CONSTRAINT UQ_customers_phone UNIQUE (phone),
    INDEX IDX_customers_code (code),
    INDEX IDX_customers_phone (phone),
    INDEX IDX_customers_is_active (is_active),
    INDEX IDX_customers_synced (synced)
  ) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE
  invoices (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    payment_method VARCHAR(50) NOT NULL DEFAULT 'CASH',
    status VARCHAR(50) NOT NULL DEFAULT 'PAID',
    cancel_reason TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    synced BOOLEAN DEFAULT FALSE,
    employee_id BIGINT NOT NULL,
    customer_id BIGINT,
    CONSTRAINT PK_invoices PRIMARY KEY (id),
    CONSTRAINT FK_invoices_employees FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT FK_invoices_customers FOREIGN KEY (customer_id) REFERENCES customers (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT UQ_invoices_code UNIQUE (code),
    INDEX IDX_invoices_code (code),
    INDEX IDX_invoices_employee_id (employee_id),
    INDEX IDX_invoices_customer_id (customer_id),
    INDEX IDX_invoices_created_at (created_at),
    INDEX IDX_invoices_status (status),
    INDEX IDX_invoices_is_active (is_active),
    INDEX IDX_invoices_synced (synced)
  ) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE
  invoice_items (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    unit_price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    subtotal DECIMAL(10, 2) NOT NULL,
    note TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    invoice_id BIGINT NOT NULL,
    variant_id BIGINT NOT NULL,
    CONSTRAINT PK_invoice_items PRIMARY KEY (id),
    CONSTRAINT FK_invoice_items_invoices FOREIGN KEY (invoice_id) REFERENCES invoices (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_invoice_items_product_variants FOREIGN KEY (variant_id) REFERENCES product_variants (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT UQ_invoice_items_code UNIQUE (code),
    CONSTRAINT CK_invoice_items_quantity CHECK (quantity > 0),
    CONSTRAINT CK_invoice_items_prices CHECK (
      unit_price >= 0
      AND subtotal >= 0
    ),
    INDEX IDX_invoice_items_code (code),
    INDEX IDX_invoice_items_invoice_id (invoice_id),
    INDEX IDX_invoice_items_variant_id (variant_id),
    INDEX IDX_invoice_items_is_active (is_active)
  ) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

SET
  FOREIGN_KEY_CHECKS = 1;