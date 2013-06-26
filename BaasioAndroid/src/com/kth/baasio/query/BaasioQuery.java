
package com.kth.baasio.query;

import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioQueryAsyncTask;
import com.kth.baasio.callback.BaasioQueryCallback;
import com.kth.baasio.entity.BaasioBaseEntity;
import com.kth.baasio.entity.group.BaasioGroup;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioError;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.response.BaasioResponse;
import com.kth.baasio.utils.ObjectUtils;

import org.springframework.http.HttpMethod;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Stack;

public class BaasioQuery implements Cloneable {

    public enum ORDER_BY {
        ASCENDING, DESCENDING
    };

    private Stack<String> cursorStack = new Stack<String>();

    private String currentCursor;

    private int limit = 10;

    private BaasioBaseEntity entity;

    private BaasioBaseEntity relatedWithThisEntity;

    private String relationship;

    private BaasioGroup inThisGroup;

    private String projectionIn;

    private String wheres;

    private String orderByKey;

    private ORDER_BY orderByMethod = ORDER_BY.ASCENDING;

    private String rawString;

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BaasioQuery() {
    }

    /**
     * Get raw query string.
     * 
     * @return the rawString
     */
    public String getRawString() {
        return rawString;
    }

    /**
     * Set raw query string. If set raw query string, other query setting will
     * reset.
     * 
     * @param rawString the rawString to set
     */
    public void setRawString(String rawString) {
        this.entity = null;
        this.inThisGroup = null;
        this.relatedWithThisEntity = null;
        this.relationship = null;

        this.rawString = rawString;
    }

    /**
     * Set limit.
     * 
     * @param limit Limit
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Get limit.
     * 
     * @return Limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Set query method by entity type.
     * 
     * @param entity Entity. Entity must have type.
     */
    public <T extends BaasioBaseEntity> void setType(T entity) {
        if (ObjectUtils.isEmpty(entity.getType())) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        this.inThisGroup = null;
        this.relatedWithThisEntity = null;
        this.relationship = null;

        this.entity = new BaasioBaseEntity(entity.getType());
    }

    /**
     * Set entity type to query.
     * 
     * @param type Entity type
     */
    public void setType(String type) {
        this.inThisGroup = null;
        this.relatedWithThisEntity = null;
        this.relationship = null;

        this.entity = new BaasioBaseEntity(type);
    }

    /**
     * Get entity type to query.
     * 
     * @return Entity type to query
     */
    public String getType() {
        return entity.getType();
    }

    /**
     * Set query method by relationship. If set relation, type and group will
     * reset.
     * 
     * @param entity Target entity. Entity must have type and uuid.
     * @param relationship Relationship name
     */
    public <T extends BaasioBaseEntity> void setRelation(T entity, String relationship) {
        if (ObjectUtils.isEmpty(entity)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TARGET_ENTITY);
        }

        if (ObjectUtils.isEmpty(entity.getType())) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        if (ObjectUtils.isEmpty(relationship)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_RELATIONSHIP);
        }

        if (entity instanceof BaasioUser) {
            BaasioUser targetUserEntity = (BaasioUser)entity;
            if (ObjectUtils.isEmpty(targetUserEntity.getUniqueKey())) {
                throw new IllegalArgumentException(BaasioError.ERROR_MISSING_USER_UUID_OR_USERNAME);
            }
        } else {
            if (ObjectUtils.isEmpty(entity.getUniqueKey())) {
                throw new IllegalArgumentException(BaasioError.ERROR_MISSING_UUID_OR_NAME);
            }
        }

        if (ObjectUtils.isEmpty(relationship)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_RELATIONSHIP);
        }

        this.entity = null;
        this.inThisGroup = null;

        this.relatedWithThisEntity = entity;
        this.relationship = relationship;
    }

    /**
     * Get relation name to query.
     * 
     * @return Relationship name
     */
    public String getRelationShip() {
        return this.relationship;
    }

    /**
     * Get target entity to query which have relationship.
     * 
     * @return Target entity
     */
    public BaasioBaseEntity getRelationEntity() {
        return this.relatedWithThisEntity;
    }

    /**
     * Set query method by group. If set group, type and group will reset.
     * 
     * @param group Target group. Group must have group path.
     */
    public void setGroup(BaasioGroup group) {
        if (ObjectUtils.isEmpty(group)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TARGET_GROUP_ENTITY);
        }

        if (ObjectUtils.isEmpty(group.getUniqueKey())) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_GROUP_UUID_OR_PATH);
        }

        this.entity = null;
        this.relatedWithThisEntity = null;
        this.relationship = null;

        this.inThisGroup = group;
    }

    /**
     * Get target group to query.
     * 
     * @return Target group
     */
    public BaasioGroup getGroup() {
        return this.inThisGroup;
    }

    /**
     * Get columns to query. A list of which columns to return.
     * 
     * @return Projection clause
     */
    public String getProjectionIn() {
        return projectionIn;
    }

    /**
     * Set columns to query. A list of which columns to return.
     * 
     * @param projectionIn A list of which columns to return
     */
    public void setProjectionIn(String projectionIn) {
        this.projectionIn = projectionIn;
    }

    /**
     * Get where clause.
     * 
     * @return the wheres
     */
    public String getWheres() {
        return wheres;
    }

    /**
     * Set where clause.
     * 
     * @param wheres Where clause
     */
    public void setWheres(String wheres) {
        this.wheres = wheres;
    }

    /**
     * Get next cursor to query.
     * 
     * @return Next cursor
     */
    private String getNextCursor() {
        int index = -1;
        if (!ObjectUtils.isEmpty(currentCursor)) {
            index = cursorStack.indexOf(currentCursor);
        }

        if (cursorStack.size() > index + 1) {
            String next = cursorStack.get(index + 1);
            if (!ObjectUtils.isEmpty(next)) {
                return next;
            }
        }

        return null;
    }

    /**
     * To check whether have next entity.
     * 
     * @return If true, have next entity.
     */
    public boolean hasNextEntities() {
        int index = -1;
        if (!ObjectUtils.isEmpty(currentCursor)) {
            index = cursorStack.indexOf(currentCursor);
        }

        if (cursorStack.size() > index + 1) {
            String next = cursorStack.get(index + 1);
            if (!ObjectUtils.isEmpty(next)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get previous cursor to query.
     * 
     * @return Previous cursor
     */
    private String getPrevCursor() {
        int index = -1;
        if (!ObjectUtils.isEmpty(currentCursor)) {
            index = cursorStack.indexOf(currentCursor);
        }

        if (index > 0) {
            String prev = cursorStack.get(index - 1);
            if (!ObjectUtils.isEmpty(prev)) {
                return prev;
            }
        }

        return null;
    }

    /**
     * To check whether have previous entity.
     * 
     * @return If true, have previous entity.
     */
    public boolean hasPrevEntities() {
        int index = -1;
        if (!ObjectUtils.isEmpty(currentCursor)) {
            index = cursorStack.indexOf(currentCursor);
        }

        if (index > -1) {
            if (index == 0) {
                return true;
            } else {
                String prev = cursorStack.get(index - 1);
                if (!ObjectUtils.isEmpty(prev)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Set orderby clause.
     * 
     * @param key Property name
     * @param orderBy Orderby method
     * @return this
     */
    public BaasioQuery setOrderBy(String key, ORDER_BY orderBy) {
        orderByKey = key;
        orderByMethod = orderBy;

        return this;
    }

    private StringBuilder getQueryBaseString() throws BaasioException {
        StringBuilder queryStringBuilder = new StringBuilder();
        queryStringBuilder.append("select ");
        if (!ObjectUtils.isEmpty(projectionIn)) {
            queryStringBuilder.append(getProjectionIn());
        } else {
            queryStringBuilder.append("*");
        }

        if (!ObjectUtils.isEmpty(getWheres())) {
            queryStringBuilder.append(" where ");
            queryStringBuilder.append(getWheres());
        }

        if (!ObjectUtils.isEmpty(orderByKey)) {
            queryStringBuilder.append(" order by ");
            queryStringBuilder.append(orderByKey);
            if (orderByMethod == ORDER_BY.ASCENDING) {
                queryStringBuilder.append(" asc");
            } else {
                queryStringBuilder.append(" desc");
            }
        }

        StringBuilder result = new StringBuilder();
        if (queryStringBuilder.length() > 0) {
            if (result.length() <= 0) {
                result.append("?ql=");
            }

            try {
                result.append(URLEncoder.encode(queryStringBuilder.toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new BaasioException(e);
            }
        }

        if (getLimit() != 10) {
            result.append("&limit=" + getLimit());
        }

        return result;
    }

    private String getQueryString(boolean next) throws BaasioException {
        StringBuilder queryStringBuilder = getQueryBaseString();

        if (getLimit() != 10) {
            queryStringBuilder.append("&limit=" + getLimit());
        }

        if (next) {
            if (hasNextEntities()) {
                queryStringBuilder.append("&cursor=" + getNextCursor());
            } else {
                throw new BaasioException(BaasioError.ERROR_QUERY_NO_MORE_NEXT);
            }
        } else {
            if (hasPrevEntities()) {
                queryStringBuilder.append("&cursor=" + getPrevCursor());
            } else {
                throw new BaasioException(BaasioError.ERROR_QUERY_NO_MORE_PREV);
            }
        }

        return queryStringBuilder.toString();
    }

    /**
     * Query entities.
     * 
     * @return Query result
     */
    public BaasioResponse query() throws BaasioException {
        BaasioResponse response = null;
        if (ObjectUtils.isEmpty(getRawString())) {
            if (!ObjectUtils.isEmpty(relatedWithThisEntity)) {
                if (ObjectUtils.isEmpty(relatedWithThisEntity.getType())) {
                    throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
                }

                response = Baas.io().apiRequest(HttpMethod.GET, null, null,
                        relatedWithThisEntity.getType(), relatedWithThisEntity.getUniqueKey(),
                        relationship + getQueryBaseString());

            } else if (!ObjectUtils.isEmpty(inThisGroup)) {
                response = Baas.io().apiRequest(HttpMethod.GET, null, null, "groups",
                        inThisGroup.getUniqueKey(), "users" + getQueryBaseString());

            } else {
                if (ObjectUtils.isEmpty(getType())) {
                    throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
                }

                response = Baas.io().apiRequest(HttpMethod.GET, null, null,
                        getType() + getQueryBaseString());
            }
        } else {
            response = Baas.io().apiRequest(HttpMethod.GET, null, null, getRawString());
        }

        if (!ObjectUtils.isEmpty(response)) {
            currentCursor = null;
            cursorStack.clear();

            String nextCursor = response.getCursor();
            if (!ObjectUtils.isEmpty(nextCursor)) {
                cursorStack.push(nextCursor);
            }

            return response;
        } else {
            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
        }
    }

    /**
     * Query entities. Executes asynchronously in background and the callbacks
     * are called in the UI thread.
     * 
     * @param callback Result callback
     */
    public void queryInBackground(final BaasioQueryCallback callback) {
        (new BaasioQueryAsyncTask(this, callback) {
            @Override
            public BaasioResponse doTask() throws Exception {
                return query();
            }
        }).execute();
    }

    /**
     * Query previous entities.
     * 
     * @return Query result
     */
    public BaasioResponse prev() throws BaasioException {
        BaasioResponse response = null;
        if (ObjectUtils.isEmpty(getRawString())) {
            if (!ObjectUtils.isEmpty(relatedWithThisEntity)) {
                if (ObjectUtils.isEmpty(relatedWithThisEntity.getType())) {
                    throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
                }

                response = Baas.io().apiRequest(HttpMethod.GET, null, null,
                        relatedWithThisEntity.getType(), relatedWithThisEntity.getUniqueKey(),
                        relationship + getQueryString(false));

            } else if (!ObjectUtils.isEmpty(inThisGroup)) {
                response = Baas.io().apiRequest(HttpMethod.GET, null, null, "groups",
                        inThisGroup.getPath(), "users" + getQueryString(false));
            } else {
                if (ObjectUtils.isEmpty(getType())) {
                    throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
                }

                response = Baas.io().apiRequest(HttpMethod.GET, null, null,
                        getType() + getQueryString(false));
            }
        } else {
            response = Baas.io().apiRequest(HttpMethod.GET, null, null, getRawString());
        }

        if (!ObjectUtils.isEmpty(response)) {
            currentCursor = getPrevCursor();

            do {
                cursorStack.pop();
            } while (!cursorStack.isEmpty() && !cursorStack.lastElement().equals(currentCursor));

            String nextCursor = response.getCursor();
            if (!ObjectUtils.isEmpty(nextCursor)) {
                cursorStack.push(nextCursor);
            }
        }

        return response;
    }

    /**
     * Query previous entities. Executes asynchronously in background and the
     * callbacks are called in the UI thread.
     * 
     * @param callback Result callback
     */
    public void prevInBackground(final BaasioQueryCallback callback) {
        (new BaasioQueryAsyncTask(this, callback) {
            @Override
            public BaasioResponse doTask() throws Exception {
                return prev();
            }
        }).execute();
    }

    /**
     * Query next entities.
     * 
     * @return Query result
     */
    public BaasioResponse next() throws BaasioException {
        BaasioResponse response = null;
        if (ObjectUtils.isEmpty(getRawString())) {
            if (!ObjectUtils.isEmpty(relatedWithThisEntity)) {
                if (ObjectUtils.isEmpty(relatedWithThisEntity.getType())) {
                    throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
                }

                response = Baas.io().apiRequest(HttpMethod.GET, null, null,
                        relatedWithThisEntity.getType(), relatedWithThisEntity.getUniqueKey(),
                        relationship + getQueryString(true));

            } else if (!ObjectUtils.isEmpty(inThisGroup)) {
                response = Baas.io().apiRequest(HttpMethod.GET, null, null, "groups",
                        inThisGroup.getUniqueKey(), "users" + getQueryString(true));
            } else {
                if (ObjectUtils.isEmpty(getType())) {
                    throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
                }

                response = Baas.io().apiRequest(HttpMethod.GET, null, null,
                        getType() + getQueryString(true));
            }
        } else {
            response = Baas.io().apiRequest(HttpMethod.GET, null, null, getRawString());
        }

        if (!ObjectUtils.isEmpty(response)) {
            currentCursor = getNextCursor();

            String nextCursor = response.getCursor();
            if (!ObjectUtils.isEmpty(nextCursor)) {
                cursorStack.push(nextCursor);
            }

            return response;
        } else {
            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
        }
    }

    /**
     * Query next entities. Executes asynchronously in background and the
     * callbacks are called in the UI thread.
     * 
     * @param callback Result callback
     */
    public void nextInBackground(final BaasioQueryCallback callback) {
        (new BaasioQueryAsyncTask(this, callback) {
            @Override
            public BaasioResponse doTask() throws Exception {
                return next();
            }
        }).execute();
    }
}
