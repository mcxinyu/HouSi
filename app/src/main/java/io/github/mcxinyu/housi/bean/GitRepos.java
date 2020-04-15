package io.github.mcxinyu.housi.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by huangyuefeng on 2017/11/20.
 * Contact me : mcxinyu@gmail.com
 */
public class GitRepos {

    /**
     * https://api.github.com/repos/vokins/yhosts/commits?path=/hosts&page=1&per_page=1
     * <p>
     * sha : c1257c4d16b140bdaab641823292b2a039423c58
     * commit : {"author":{"name":"vokins","email":"vokins@gmail.com","date":"2017-11-19T08:31:18Z"},"committer":{"name":"vokins","email":"vokins@gmail.com","date":"2017-11-19T08:31:18Z"},"message":"增加一个第三方数据 3rd.txt 用于存放我未验证的别人提供的数据,版权归提供者.","tree":{"sha":"b34ebdeab9d65552bd54591f23777b2321968b04","url":"https://api.github.com/repos/vokins/yhosts/git/trees/b34ebdeab9d65552bd54591f23777b2321968b04"},"url":"https://api.github.com/repos/vokins/yhosts/git/commits/c1257c4d16b140bdaab641823292b2a039423c58","comment_count":0,"verification":{"verified":false,"reason":"unsigned","signature":null,"payload":null}}
     * url : https://api.github.com/repos/vokins/yhosts/commits/c1257c4d16b140bdaab641823292b2a039423c58
     * html_url : https://github.com/vokins/yhosts/commit/c1257c4d16b140bdaab641823292b2a039423c58
     * comments_url : https://api.github.com/repos/vokins/yhosts/commits/c1257c4d16b140bdaab641823292b2a039423c58/comments
     * author : {"login":"vokins","id":7933104,"avatar_url":"https://avatars0.githubusercontent.com/u/7933104?v=4","gravatar_id":"","url":"https://api.github.com/users/vokins","html_url":"https://github.com/vokins","followers_url":"https://api.github.com/users/vokins/followers","following_url":"https://api.github.com/users/vokins/following{/other_user}","gists_url":"https://api.github.com/users/vokins/gists{/gist_id}","starred_url":"https://api.github.com/users/vokins/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/vokins/subscriptions","organizations_url":"https://api.github.com/users/vokins/orgs","repos_url":"https://api.github.com/users/vokins/repos","events_url":"https://api.github.com/users/vokins/events{/privacy}","received_events_url":"https://api.github.com/users/vokins/received_events","type":"User","site_admin":false}
     * committer : {"login":"vokins","id":7933104,"avatar_url":"https://avatars0.githubusercontent.com/u/7933104?v=4","gravatar_id":"","url":"https://api.github.com/users/vokins","html_url":"https://github.com/vokins","followers_url":"https://api.github.com/users/vokins/followers","following_url":"https://api.github.com/users/vokins/following{/other_user}","gists_url":"https://api.github.com/users/vokins/gists{/gist_id}","starred_url":"https://api.github.com/users/vokins/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/vokins/subscriptions","organizations_url":"https://api.github.com/users/vokins/orgs","repos_url":"https://api.github.com/users/vokins/repos","events_url":"https://api.github.com/users/vokins/events{/privacy}","received_events_url":"https://api.github.com/users/vokins/received_events","type":"User","site_admin":false}
     * parents : [{"sha":"7b044426b83732cb5ed434d30bb02d8d5f9bf0e1","url":"https://api.github.com/repos/vokins/yhosts/commits/7b044426b83732cb5ed434d30bb02d8d5f9bf0e1","html_url":"https://github.com/vokins/yhosts/commit/7b044426b83732cb5ed434d30bb02d8d5f9bf0e1"}]
     */

    @SerializedName("sha")
    private String sha;
    @SerializedName("commit")
    private CommitBean commit;
    @SerializedName("url")
    private String url;
    @SerializedName("html_url")
    private String htmlUrl;
    @SerializedName("comments_url")
    private String commentsUrl;
    @SerializedName("author")
    private AuthorBean author;
    @SerializedName("committer")
    private CommitterBean committer;
    @SerializedName("parents")
    private List<ParentsBean> parents;

    public void setSha(String sha) {
        this.sha = sha;
    }

    public void setCommit(CommitBean commit) {
        this.commit = commit;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        this.commentsUrl = commentsUrl;
    }

    public void setAuthor(AuthorBean author) {
        this.author = author;
    }

    public void setCommitter(CommitterBean committer) {
        this.committer = committer;
    }

    public void setParents(List<ParentsBean> parents) {
        this.parents = parents;
    }

    public String getSha() {
        return sha;
    }

    public CommitBean getCommit() {
        return commit;
    }

    public String getUrl() {
        return url;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public AuthorBean getAuthor() {
        return author;
    }

    public CommitterBean getCommitter() {
        return committer;
    }

    public List<ParentsBean> getParents() {
        return parents;
    }

    public static class CommitBean {
        /**
         * author : {"name":"vokins","email":"vokins@gmail.com","date":"2017-11-19T08:31:18Z"}
         * committer : {"name":"vokins","email":"vokins@gmail.com","date":"2017-11-19T08:31:18Z"}
         * message : 增加一个第三方数据 3rd.txt 用于存放我未验证的别人提供的数据,版权归提供者.
         * tree : {"sha":"b34ebdeab9d65552bd54591f23777b2321968b04","url":"https://api.github.com/repos/vokins/yhosts/git/trees/b34ebdeab9d65552bd54591f23777b2321968b04"}
         * url : https://api.github.com/repos/vokins/yhosts/git/commits/c1257c4d16b140bdaab641823292b2a039423c58
         * comment_count : 0
         * verification : {"verified":false,"reason":"unsigned","signature":null,"payload":null}
         */

        @SerializedName("author")
        private AuthorBean author;
        @SerializedName("committer")
        private CommitterBean committer;
        /**
         * 兼容码云的 api
         * commiter : {"name":"lankes","date":"2017-10-19T15:03:32+08:00","email":"303877543@qq.com"}
         */
        @SerializedName("commiter")
        private CommitterBean commiter;
        @SerializedName("message")
        private String message;
        @SerializedName("tree")
        private TreeBean tree;
        @SerializedName("url")
        private String url;
        @SerializedName("comment_count")
        private int commentCount;
        @SerializedName("verification")
        private VerificationBean verification;

        public void setAuthor(AuthorBean author) {
            this.author = author;
        }

        public void setCommitter(CommitterBean committer) {
            this.committer = committer;
        }

        public void setCommiter(CommitterBean commiter) {
            this.commiter = commiter;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setTree(TreeBean tree) {
            this.tree = tree;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public void setVerification(VerificationBean verification) {
            this.verification = verification;
        }

        public AuthorBean getAuthor() {
            return author;
        }

        public CommitterBean getCommitter() {
            if (committer == null) {
                return commiter;
            }
            return committer;
        }

        public CommitterBean getCommiter() {
            if (commiter == null) {
                return committer;
            }
            return commiter;
        }

        public String getMessage() {
            return message;
        }

        public TreeBean getTree() {
            return tree;
        }

        public String getUrl() {
            return url;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public VerificationBean getVerification() {
            return verification;
        }

        public static class AuthorBean {
            /**
             * name : vokins
             * email : vokins@gmail.com
             * date : 2017-11-19T08:31:18Z
             */

            @SerializedName("name")
            private String name;
            @SerializedName("email")
            private String email;
            @SerializedName("date")
            private String date;

            public void setName(String name) {
                this.name = name;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getName() {
                return name;
            }

            public String getEmail() {
                return email;
            }

            public String getDate() {
                return date;
            }
        }

        public static class CommitterBean {
            /**
             * name : vokins
             * email : vokins@gmail.com
             * date : 2017-11-19T08:31:18Z
             */

            @SerializedName("name")
            private String name;
            @SerializedName("email")
            private String email;
            @SerializedName("date")
            private String date;

            public void setName(String name) {
                this.name = name;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getName() {
                return name;
            }

            public String getEmail() {
                return email;
            }

            public String getDate() {
                return date;
            }
        }

        public static class TreeBean {
            /**
             * sha : b34ebdeab9d65552bd54591f23777b2321968b04
             * url : https://api.github.com/repos/vokins/yhosts/git/trees/b34ebdeab9d65552bd54591f23777b2321968b04
             */

            @SerializedName("sha")
            private String sha;
            @SerializedName("url")
            private String url;

            public void setSha(String sha) {
                this.sha = sha;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getSha() {
                return sha;
            }

            public String getUrl() {
                return url;
            }
        }

        public static class VerificationBean {
            /**
             * verified : false
             * reason : unsigned
             * signature : null
             * payload : null
             */

            @SerializedName("verified")
            private boolean verified;
            @SerializedName("reason")
            private String reason;
            @SerializedName("signature")
            private Object signature;
            @SerializedName("payload")
            private Object payload;

            public void setVerified(boolean verified) {
                this.verified = verified;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }

            public void setSignature(Object signature) {
                this.signature = signature;
            }

            public void setPayload(Object payload) {
                this.payload = payload;
            }

            public boolean getVerified() {
                return verified;
            }

            public String getReason() {
                return reason;
            }

            public Object getSignature() {
                return signature;
            }

            public Object getPayload() {
                return payload;
            }
        }
    }

    public static class AuthorBean {
        /**
         * login : vokins
         * id : 7933104
         * avatar_url : https://avatars0.githubusercontent.com/u/7933104?v=4
         * gravatar_id :
         * url : https://api.github.com/users/vokins
         * html_url : https://github.com/vokins
         * followers_url : https://api.github.com/users/vokins/followers
         * following_url : https://api.github.com/users/vokins/following{/other_user}
         * gists_url : https://api.github.com/users/vokins/gists{/gist_id}
         * starred_url : https://api.github.com/users/vokins/starred{/owner}{/repo}
         * subscriptions_url : https://api.github.com/users/vokins/subscriptions
         * organizations_url : https://api.github.com/users/vokins/orgs
         * repos_url : https://api.github.com/users/vokins/repos
         * events_url : https://api.github.com/users/vokins/events{/privacy}
         * received_events_url : https://api.github.com/users/vokins/received_events
         * type : User
         * site_admin : false
         */

        @SerializedName("login")
        private String login;
        @SerializedName("id")
        private int id;
        @SerializedName("avatar_url")
        private String avatarUrl;
        @SerializedName("gravatar_id")
        private String gravatarId;
        @SerializedName("url")
        private String url;
        @SerializedName("html_url")
        private String htmlUrl;
        @SerializedName("followers_url")
        private String followersUrl;
        @SerializedName("following_url")
        private String followingUrl;
        @SerializedName("gists_url")
        private String gistsUrl;
        @SerializedName("starred_url")
        private String starredUrl;
        @SerializedName("subscriptions_url")
        private String subscriptionsUrl;
        @SerializedName("organizations_url")
        private String organizationsUrl;
        @SerializedName("repos_url")
        private String reposUrl;
        @SerializedName("events_url")
        private String eventsUrl;
        @SerializedName("received_events_url")
        private String receivedEventsUrl;
        @SerializedName("type")
        private String type;
        @SerializedName("site_admin")
        private boolean siteAdmin;

        public void setLogin(String login) {
            this.login = login;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public void setGravatarId(String gravatarId) {
            this.gravatarId = gravatarId;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setHtmlUrl(String htmlUrl) {
            this.htmlUrl = htmlUrl;
        }

        public void setFollowersUrl(String followersUrl) {
            this.followersUrl = followersUrl;
        }

        public void setFollowingUrl(String followingUrl) {
            this.followingUrl = followingUrl;
        }

        public void setGistsUrl(String gistsUrl) {
            this.gistsUrl = gistsUrl;
        }

        public void setStarredUrl(String starredUrl) {
            this.starredUrl = starredUrl;
        }

        public void setSubscriptionsUrl(String subscriptionsUrl) {
            this.subscriptionsUrl = subscriptionsUrl;
        }

        public void setOrganizationsUrl(String organizationsUrl) {
            this.organizationsUrl = organizationsUrl;
        }

        public void setReposUrl(String reposUrl) {
            this.reposUrl = reposUrl;
        }

        public void setEventsUrl(String eventsUrl) {
            this.eventsUrl = eventsUrl;
        }

        public void setReceivedEventsUrl(String receivedEventsUrl) {
            this.receivedEventsUrl = receivedEventsUrl;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setSiteAdmin(boolean siteAdmin) {
            this.siteAdmin = siteAdmin;
        }

        public String getLogin() {
            return login;
        }

        public int getId() {
            return id;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public String getGravatarId() {
            return gravatarId;
        }

        public String getUrl() {
            return url;
        }

        public String getHtmlUrl() {
            return htmlUrl;
        }

        public String getFollowersUrl() {
            return followersUrl;
        }

        public String getFollowingUrl() {
            return followingUrl;
        }

        public String getGistsUrl() {
            return gistsUrl;
        }

        public String getStarredUrl() {
            return starredUrl;
        }

        public String getSubscriptionsUrl() {
            return subscriptionsUrl;
        }

        public String getOrganizationsUrl() {
            return organizationsUrl;
        }

        public String getReposUrl() {
            return reposUrl;
        }

        public String getEventsUrl() {
            return eventsUrl;
        }

        public String getReceivedEventsUrl() {
            return receivedEventsUrl;
        }

        public String getType() {
            return type;
        }

        public boolean getSiteAdmin() {
            return siteAdmin;
        }
    }

    public static class CommitterBean {
        /**
         * login : vokins
         * id : 7933104
         * avatar_url : https://avatars0.githubusercontent.com/u/7933104?v=4
         * gravatar_id :
         * url : https://api.github.com/users/vokins
         * html_url : https://github.com/vokins
         * followers_url : https://api.github.com/users/vokins/followers
         * following_url : https://api.github.com/users/vokins/following{/other_user}
         * gists_url : https://api.github.com/users/vokins/gists{/gist_id}
         * starred_url : https://api.github.com/users/vokins/starred{/owner}{/repo}
         * subscriptions_url : https://api.github.com/users/vokins/subscriptions
         * organizations_url : https://api.github.com/users/vokins/orgs
         * repos_url : https://api.github.com/users/vokins/repos
         * events_url : https://api.github.com/users/vokins/events{/privacy}
         * received_events_url : https://api.github.com/users/vokins/received_events
         * type : User
         * site_admin : false
         */

        @SerializedName("login")
        private String login;
        @SerializedName("id")
        private int id;
        @SerializedName("avatar_url")
        private String avatarUrl;
        @SerializedName("gravatar_id")
        private String gravatarId;
        @SerializedName("url")
        private String url;
        @SerializedName("html_url")
        private String htmlUrl;
        @SerializedName("followers_url")
        private String followersUrl;
        @SerializedName("following_url")
        private String followingUrl;
        @SerializedName("gists_url")
        private String gistsUrl;
        @SerializedName("starred_url")
        private String starredUrl;
        @SerializedName("subscriptions_url")
        private String subscriptionsUrl;
        @SerializedName("organizations_url")
        private String organizationsUrl;
        @SerializedName("repos_url")
        private String reposUrl;
        @SerializedName("events_url")
        private String eventsUrl;
        @SerializedName("received_events_url")
        private String receivedEventsUrl;
        @SerializedName("type")
        private String type;
        @SerializedName("site_admin")
        private boolean siteAdmin;

        public void setLogin(String login) {
            this.login = login;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public void setGravatarId(String gravatarId) {
            this.gravatarId = gravatarId;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setHtmlUrl(String htmlUrl) {
            this.htmlUrl = htmlUrl;
        }

        public void setFollowersUrl(String followersUrl) {
            this.followersUrl = followersUrl;
        }

        public void setFollowingUrl(String followingUrl) {
            this.followingUrl = followingUrl;
        }

        public void setGistsUrl(String gistsUrl) {
            this.gistsUrl = gistsUrl;
        }

        public void setStarredUrl(String starredUrl) {
            this.starredUrl = starredUrl;
        }

        public void setSubscriptionsUrl(String subscriptionsUrl) {
            this.subscriptionsUrl = subscriptionsUrl;
        }

        public void setOrganizationsUrl(String organizationsUrl) {
            this.organizationsUrl = organizationsUrl;
        }

        public void setReposUrl(String reposUrl) {
            this.reposUrl = reposUrl;
        }

        public void setEventsUrl(String eventsUrl) {
            this.eventsUrl = eventsUrl;
        }

        public void setReceivedEventsUrl(String receivedEventsUrl) {
            this.receivedEventsUrl = receivedEventsUrl;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setSiteAdmin(boolean siteAdmin) {
            this.siteAdmin = siteAdmin;
        }

        public String getLogin() {
            return login;
        }

        public int getId() {
            return id;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public String getGravatarId() {
            return gravatarId;
        }

        public String getUrl() {
            return url;
        }

        public String getHtmlUrl() {
            return htmlUrl;
        }

        public String getFollowersUrl() {
            return followersUrl;
        }

        public String getFollowingUrl() {
            return followingUrl;
        }

        public String getGistsUrl() {
            return gistsUrl;
        }

        public String getStarredUrl() {
            return starredUrl;
        }

        public String getSubscriptionsUrl() {
            return subscriptionsUrl;
        }

        public String getOrganizationsUrl() {
            return organizationsUrl;
        }

        public String getReposUrl() {
            return reposUrl;
        }

        public String getEventsUrl() {
            return eventsUrl;
        }

        public String getReceivedEventsUrl() {
            return receivedEventsUrl;
        }

        public String getType() {
            return type;
        }

        public boolean getSiteAdmin() {
            return siteAdmin;
        }
    }

    public static class ParentsBean {
        /**
         * sha : 7b044426b83732cb5ed434d30bb02d8d5f9bf0e1
         * url : https://api.github.com/repos/vokins/yhosts/commits/7b044426b83732cb5ed434d30bb02d8d5f9bf0e1
         * html_url : https://github.com/vokins/yhosts/commit/7b044426b83732cb5ed434d30bb02d8d5f9bf0e1
         */

        @SerializedName("sha")
        private String sha;
        @SerializedName("url")
        private String url;
        @SerializedName("html_url")
        private String htmlUrl;

        public void setSha(String sha) {
            this.sha = sha;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setHtmlUrl(String htmlUrl) {
            this.htmlUrl = htmlUrl;
        }

        public String getSha() {
            return sha;
        }

        public String getUrl() {
            return url;
        }

        public String getHtmlUrl() {
            return htmlUrl;
        }
    }
}
