package com.factoriaf5.ecommerceManager.products;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductCustomRepo {
    private final EntityManager entityManager;

    public ProductCustomRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public List<Product> findFeaturedProductsByCategory(String categoryName) {
        // Step 1: Get CriteriaBuilder
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Step 2: Create CriteriaQuery
        CriteriaQuery<Product> query = cb.createQuery(Product.class);

        // Step 3: Define the root of the query (Product entity)
        Root<Product> productRoot = query.from(Product.class);

        // Step 4: Add predicates (conditions)
        Predicate categoryPredicate = cb.equal(productRoot.get("category"), categoryName);
        Predicate featuredPredicate = cb.isTrue(productRoot.get("featured"));

        // Combine predicates
        query.select(productRoot)
                .where(cb.and(categoryPredicate, featuredPredicate));

        // Step 5: Execute the query
        return entityManager.createQuery(query).getResultList();
    }
}
